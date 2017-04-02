package no.fint.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.arbeidsforhold.ArbeidsforholdCacheService;
import no.fint.consumer.event.EventActions;
import no.fint.consumer.person.PersonCacheService;
import no.fint.consumer.personalressurs.PersonalressursCacheService;
import no.fint.consumer.utils.CacheUri;
import no.fint.event.model.Event;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.felles.Person;
import no.fint.model.relation.FintResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SubscriberService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonalressursCacheService personalressursCacheService;

    @Autowired
    private PersonCacheService personCacheService;

    @Autowired
    private ArbeidsforholdCacheService arbeidsforholdCacheService;

    public void receive(Event event) {
        log.info("Event: {}", event.getAction());
        try {
            EventActions action = EventActions.valueOf(event.getAction());

            if (action == EventActions.GET_ALL_PERSONALRESSURS) {
                List<?> employees = event.getData();
                List<FintResource> convertedList = employees.stream().map(employee -> objectMapper.convertValue(employee, FintResource.class)).collect(Collectors.toList());
                List<FintResource<Personalressurs>> personalressursList = mapFintResource(Personalressurs.class, convertedList);
                personalressursCacheService.getCache(CacheUri.create(event.getOrgId(), "personalressurs")).ifPresent(cache -> cache.update(personalressursList));
            } else if (action == EventActions.GET_ALL_PERSON) {
                List<?> persons = event.getData();
                List<FintResource> convertedList = persons.stream().map(person -> objectMapper.convertValue(person, FintResource.class)).collect(Collectors.toList());
                List<FintResource<Person>> personList = mapFintResource(Person.class, convertedList);
                personCacheService.getCache(CacheUri.create(event.getOrgId(), "person")).ifPresent(cache -> cache.update(personList));
            } else if (action == EventActions.GET_ALL_ARBEIDSFORHOLD) {
                List<?> employments = event.getData();
                List<FintResource> convertedList = employments.stream().map(employment -> objectMapper.convertValue(employment, FintResource.class)).collect(Collectors.toList());
                List<FintResource<Arbeidsforhold>> arbeidsforholdList = mapFintResource(Arbeidsforhold.class, convertedList);
                arbeidsforholdCacheService.getCache(CacheUri.create(event.getOrgId(), "arbeidsforhold")).ifPresent(cache -> cache.update(arbeidsforholdList));
            } else {
                log.warn("Unhandled event: {}", event.getAction());
            }
        } catch (IllegalArgumentException e) {
            log.error("Unhandled event: " + event.getAction(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<FintResource<T>> mapFintResource(Class<T> type, List<FintResource> fintResources) {
        List<FintResource<T>> resources = new ArrayList<>();
        for (FintResource fintResource : fintResources) {
            FintResource<T> resource = new FintResource<>(type, (T) fintResource.getResource());
            resource.setRelasjoner(fintResource.getRelasjoner());
            resources.add(resource);
        }
        return resources;
    }

}
