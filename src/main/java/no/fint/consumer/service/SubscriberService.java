package no.fint.consumer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.arbeidsforhold.ArbeidsforholdCacheService;
import no.fint.consumer.event.EventActions;
import no.fint.consumer.person.PersonCacheService;
import no.fint.consumer.personalressurs.PersonalressursCacheService;
import no.fint.consumer.utils.CacheUri;
import no.fint.event.model.Event;
import no.fint.event.model.EventUtil;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.felles.Person;
import no.fint.model.relation.FintResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SubscriberService {

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
                List<FintResource<Personalressurs>> personalressursList = EventUtil.convertEventData(event, new TypeReference<List<FintResource<Personalressurs>>>() {
                });
                personalressursCacheService.getCache(CacheUri.create(event.getOrgId(), "personalressurs")).ifPresent(cache -> cache.update(personalressursList));
            } else if (action == EventActions.GET_ALL_PERSON) {
                List<FintResource<Person>> personList = EventUtil.convertEventData(event, new TypeReference<List<FintResource<Person>>>() {
                });
                personCacheService.getCache(CacheUri.create(event.getOrgId(), "person")).ifPresent(cache -> cache.update(personList));
            } else if (action == EventActions.GET_ALL_ARBEIDSFORHOLD) {
                List<FintResource<Arbeidsforhold>> arbeidsforholdList = EventUtil.convertEventData(event, new TypeReference<List<FintResource<Arbeidsforhold>>>() {
                });
                arbeidsforholdCacheService.getCache(CacheUri.create(event.getOrgId(), "arbeidsforhold")).ifPresent(cache -> cache.update(arbeidsforholdList));
            } else {
                log.warn("Unhandled event: {}", event.getAction());
            }
        } catch (IllegalArgumentException e) {
            log.error("Unhandled event: " + event.getAction(), e);
        }
    }
}
