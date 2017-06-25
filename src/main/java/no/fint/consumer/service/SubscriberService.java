package no.fint.consumer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.utils.CacheUri;
import no.fint.consumer.arbeidsforhold.ArbeidsforholdCacheService;
import no.fint.consumer.person.PersonCacheService;
import no.fint.consumer.personalressurs.PersonalressursCacheService;
import no.fint.event.model.Event;
import no.fint.event.model.EventUtil;
import no.fint.events.annotations.FintEventListener;
import no.fint.events.queue.QueueType;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.PersonalActions;
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

    @FintEventListener(type = QueueType.UPSTREAM)
    public void receive(Event event) {
        log.info("Event: {}", event.getAction());
        try {
            PersonalActions action = PersonalActions.valueOf(event.getAction());
            if (action == PersonalActions.GET_ALL_PERSONALRESSURS) {
                List<FintResource<Personalressurs>> personalressursList = EventUtil.convertEventData(event, new TypeReference<List<FintResource<Personalressurs>>>() {
                });
                personalressursCacheService.getCache(CacheUri.create(event.getOrgId(), PersonalressursCacheService.MODEL)).ifPresent(cache -> cache.update(personalressursList));
            } else if (action == PersonalActions.GET_ALL_PERSON) {
                List<FintResource<Person>> personList = EventUtil.convertEventData(event, new TypeReference<List<FintResource<Person>>>() {
                });
                personCacheService.getCache(CacheUri.create(event.getOrgId(), PersonCacheService.MODEL)).ifPresent(cache -> cache.update(personList));
            } else if (action == PersonalActions.GET_ALL_ARBEIDSFORHOLD) {
                List<FintResource<Arbeidsforhold>> arbeidsforholdList = EventUtil.convertEventData(event, new TypeReference<List<FintResource<Arbeidsforhold>>>() {
                });
                arbeidsforholdCacheService.getCache(CacheUri.create(event.getOrgId(), ArbeidsforholdCacheService.MODEL)).ifPresent(cache -> cache.update(arbeidsforholdList));
            } else {
                log.warn("Unhandled event: {}", event.getAction());
            }
        } catch (IllegalArgumentException e) {
            log.error("Unhandled event: " + event.getAction(), e);
        }
    }
}
