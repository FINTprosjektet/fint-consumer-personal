package no.fint.consumer.personalressurs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.utils.CacheUri;
import no.fint.event.model.Event;
import no.fint.personal.Personalressurs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PersonalressursSubscriber {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonalressursCacheService cacheService;

    public void receive(Event event) {
        List<?> employees = event.getData();
        List<Personalressurs> personalressursList = employees.stream().map(employee -> objectMapper.convertValue(employee, Personalressurs.class)).collect(Collectors.toList());
        cacheService.getCache(CacheUri.create(event.getOrgId(), "personalressurs")).ifPresent(cache -> cache.update(personalressursList));
    }

}
