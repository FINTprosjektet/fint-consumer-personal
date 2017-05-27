package no.fint.consumer.person;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.FintCache;
import no.fint.consumer.CacheService;
import no.fint.consumer.event.Actions;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.consumer.utils.CacheUri;
import no.fint.event.model.Event;
import no.fint.model.felles.Person;
import no.fint.model.relation.FintResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Slf4j
@Service
public class PersonCacheService extends CacheService<FintResource<Person>> {

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Value("${fint.events.orgs:mock.no}")
    private String[] orgs;

    @PostConstruct
    public void init() {
        Arrays.stream(orgs).forEach(orgId -> {
            FintCache<FintResource<Person>> cache = new FintCache<>();
            String cacheUri = CacheUri.create(orgId, "person");
            caches.put(cacheUri, cache);
        });
    }

    @Scheduled(initialDelayString = "${fint.consumer.cache.initialDelay.person:40000}", fixedRateString = "${fint.consumer.cache.fixedRate.person:55000}")
    public void getAllPersons() {
        Arrays.stream(orgs).forEach(orgId -> {
            log.info("Populating person cache for {}", orgId);
            Event event = new Event(orgId, "administrasjon/personal", Actions.GET_ALL_PERSON.name(), "CACHE_SERVICE");
            consumerEventUtil.send(event);
        });
    }

}
