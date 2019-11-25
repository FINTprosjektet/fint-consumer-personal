package no.fint.consumer.testmode.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.testmode.EnabledIfTestMode;
import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.event.model.Status;
import no.fint.events.FintEventListener;
import no.fint.events.FintEvents;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.PersonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@EnabledIfTestMode
@Slf4j
@Component
public class TestModeAdapter implements FintEventListener {

    @Autowired
    private FintEvents fintEvents;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConsumerProps consumerProps;

    @Value("${fint.consumer.test-data:.}")
    private Path basedir;

    private ImmutableMap<String,Class<?>> typeMap;

    @PostConstruct
    public void init() {
        log.info("Test-mode enabled, starting adapter");
        fintEvents.registerDownstreamSystemListener(this);
        consumerProps.getAssets().forEach(org -> fintEvents.registerDownstreamListener(org, this));
        typeMap = ImmutableMap.
                <String,Class<?>>builder()
                .put("GET_ALL_PERSON", PersonResource.class)
                .put("GET_ALL_ARBEIDSFORHOLD", ArbeidsforholdResource.class)
                .put("GET_ALL_PERSONALRESSURS", PersonalressursResource.class)
                .build();
    }

    @Override
    public void accept(Event event) {
        log.info("Received event: {}", event);
        if (event.isHealthCheck()) {
            fintEvents.sendUpstream(event);
        } else if (event.isRegisterOrgId()) {
            if (!StringUtils.isBlank(event.getOrgId())) {
                fintEvents.registerDownstreamListener(event.getOrgId(), this);
            }
        } else if (typeMap.containsKey(event.getAction())) {
            sendResponse(event);
        }
    }

    private void sendResponse(Event event) {
        try {
            String type = StringUtils.lowerCase(StringUtils.removeStart(event.getAction(), "GET_ALL_"));
            Class typeClass = typeMap.get(event.getAction());
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, typeClass);
            Path datasource = basedir.resolve(event.getOrgId()).resolve(type + ".json");
            log.info("Attempting to read {} ...", datasource);
            BufferedReader reader = Files.newBufferedReader(datasource);
            List resources = objectMapper.readValue(reader, collectionType);
            Event response = new Event(event);
            response.setResponseStatus(ResponseStatus.ACCEPTED);
            response.setStatus(Status.ADAPTER_ACCEPTED);
            response.setData(new ArrayList(resources));
            log.info("Event response: {}", response);
            fintEvents.sendUpstream(response);
        } catch (IOException e) {
            log.warn("No such luck", e);
        }
    }

}
