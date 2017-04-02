package no.fint.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.arbeidsforhold.ArbeidsforholdCacheService;
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

    @SuppressWarnings("unchecked")
    public void receive(Event event) {
        log.info("Event: {}", event.getAction());
        if (event.getAction().equals("GET_ALL_PERSONALRESSURS")) {
            List<?> employees = event.getData();
            List<FintResource> convertedList = employees.stream().map(employee -> objectMapper.convertValue(employee, FintResource.class)).collect(Collectors.toList());
            List<FintResource<Personalressurs>> personalressursList = new ArrayList<>();
            for (FintResource<Personalressurs> resource : convertedList) {
                FintResource<Personalressurs> fintResource = new FintResource<>(Personalressurs.class, resource.getResource());
                fintResource.setRelasjoner(resource.getRelasjoner());
                personalressursList.add(fintResource);
            }

            personalressursCacheService.getCache(CacheUri.create(event.getOrgId(), "personalressurs")).ifPresent(cache -> cache.update(personalressursList));
        } else if (event.getAction().equals("GET_ALL_PERSON")) {
            List<?> persons = event.getData();
            List<FintResource> convertedList = persons.stream().map(person -> objectMapper.convertValue(person, FintResource.class)).collect(Collectors.toList());
            List<FintResource<Person>> personList = new ArrayList<>();
            for (FintResource<Person> resource : convertedList) {
                FintResource<Person> fintResource = new FintResource<>(Person.class, resource.getResource());
                fintResource.setRelasjoner(resource.getRelasjoner());
                personList.add(fintResource);
            }

            personCacheService.getCache(CacheUri.create(event.getOrgId(), "person")).ifPresent(cache -> cache.update(personList));
        } else if (event.getAction().equals("GET_ALL_ARBEIDSFORHOLD")) {
            List<?> employments = event.getData();
            List<FintResource> convertedList = employments.stream().map(employment -> objectMapper.convertValue(employment, FintResource.class)).collect(Collectors.toList());
            List<FintResource<Arbeidsforhold>> arbeidsforholdList = new ArrayList<>();
            for (FintResource<Arbeidsforhold> resource : convertedList) {
                FintResource<Arbeidsforhold> fintResource = new FintResource<>(Arbeidsforhold.class, resource.getResource());
                fintResource.setRelasjoner(resource.getRelasjoner());
                arbeidsforholdList.add(fintResource);
            }

            arbeidsforholdCacheService.getCache(CacheUri.create(event.getOrgId(), "arbeidsforhold")).ifPresent(cache -> cache.update(arbeidsforholdList));
        } else {
            log.warn("Unhandled event: {}", event.getAction());
        }
    }

}
