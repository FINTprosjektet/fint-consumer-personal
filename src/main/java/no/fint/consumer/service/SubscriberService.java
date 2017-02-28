package no.fint.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.arbeidsforhold.ArbeidsforholdCacheService;
import no.fint.consumer.person.PersonCacheService;
import no.fint.consumer.personalressurs.PersonalressursCacheService;
import no.fint.consumer.relation.RelationCacheService;
import no.fint.consumer.utils.CacheUri;
import no.fint.event.model.Event;
import no.fint.felles.Person;
import no.fint.personal.Arbeidsforhold;
import no.fint.personal.Personalressurs;
import no.fint.relation.model.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private RelationCacheService relationCacheService;

    @Autowired
    private ArbeidsforholdCacheService arbeidsforholdCacheService;

    public void receive(Event event) {
        if (event.getAction().equals("GET_ALL_EMPLOYEES")) {
            List<?> employees = event.getData();
            List<Personalressurs> personalressursList = employees.stream().map(employee -> objectMapper.convertValue(employee, Personalressurs.class)).collect(Collectors.toList());
            personalressursCacheService.getCache(CacheUri.create(event.getOrgId(), "personalressurs")).ifPresent(cache -> cache.update(personalressursList));
        } else if (event.getAction().equals("GET_ALL_PERSONS")) {
            List<?> persons = event.getData();
            List<Person> personList = persons.stream().map(person -> objectMapper.convertValue(person, Person.class)).collect(Collectors.toList());
            personCacheService.getCache(CacheUri.create(event.getOrgId(), "person")).ifPresent(cache -> cache.update(personList));
        } else if (event.getAction().equals("GET_RELATIONS")) {
            List<?> relations = event.getData();
            List<Relation> relationList = relations.stream().map(relation -> objectMapper.convertValue(relation, Relation.class)).collect(Collectors.toList());
            relationCacheService.getCache(CacheUri.create(event.getOrgId(), "relation")).ifPresent(cache -> cache.update(relationList));
        } else if (event.getAction().equals("GET_ALL_EMPLOYMENTS")) {
            List<?> employments = event.getData();
            List<Arbeidsforhold> employmentList = employments.stream().map(employment -> objectMapper.convertValue(employment, Arbeidsforhold.class)).collect(Collectors.toList());
            arbeidsforholdCacheService.getCache(CacheUri.create(event.getOrgId(), "arbeidsforhold")).ifPresent(cache -> cache.update(employmentList));
        } else {
            log.warn("Unhandled event: {}", event.getAction());
        }
    }

}
