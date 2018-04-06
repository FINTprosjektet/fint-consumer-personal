package no.fint.consumer.models.fastlonn;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheService;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.model.administrasjon.personal.Fastlonn;
import no.fint.model.administrasjon.personal.PersonalActions;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.relation.FintResource;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FastlonnCacheService extends CacheService<FastlonnResource> {

    public static final String MODEL = Fastlonn.class.getSimpleName().toLowerCase();

    @Value("${fint.consumer.compatibility.fintresource:true}")
    private boolean fintResourceCompatibility;

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    public FastlonnCacheService() {
        super(MODEL, PersonalActions.GET_ALL_FASTLONN);
    }

    @PostConstruct
    public void init() {
        Arrays.stream(props.getOrgs()).forEach(this::createCache);
    }

    @Scheduled(initialDelayString = ConsumerProps.CACHE_INITIALDELAY_FASTLONN, fixedRateString = ConsumerProps.CACHE_FIXEDRATE_FASTLONN)
    public void populateCacheAll() {
        Arrays.stream(props.getOrgs()).forEach(this::populateCache);
    }

    public void rebuildCache(String orgId) {
        flush(orgId);
        populateCache(orgId);
    }

    private void populateCache(String orgId) {
        log.info("Populating Fastlonn cache for {}", orgId);
        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_FASTLONN, Constants.CACHE_SERVICE);
        consumerEventUtil.send(event);
    }


    public Optional<FastlonnResource> getFastlonnBySystemId(String orgId, String systemId) {
        return getOne(orgId, (resource) -> Optional
                .ofNullable(resource)
                .map(FastlonnResource::getSystemId)
                .map(Identifikator::getIdentifikatorverdi)
                .map(id -> id.equals(systemId))
                .orElse(false));
    }


    @Override
    public void onAction(Event event) {
        if (fintResourceCompatibility && !event.getData().isEmpty() && event.getData().get(0) instanceof FintResource) {
            log.info("Compatibility: Converting FintResource<FastlonnResource> to FastlonnResource ...");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            List<FintResource<FastlonnResource>> original = objectMapper.convertValue(event.getData(), new TypeReference<List<FintResource<FastlonnResource>>>() {
            });
            List<FastlonnResource> replacement = original.stream().map(fintResource -> {
                FastlonnResource fastlonnResource = fintResource.getResource();
                fintResource.getRelations().forEach(relation -> fastlonnResource.addLink(relation.getRelationName(), Link.with(relation.getLink())));
                return fastlonnResource;
            }).collect(Collectors.toList());
            event.setData(replacement);
        }
        update(event, new TypeReference<List<FastlonnResource>>() {
        });
    }
}
