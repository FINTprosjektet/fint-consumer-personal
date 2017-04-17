package no.fint.consumer.personalressurs;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.FintCache;
import no.fint.consumer.CacheService;
import no.fint.consumer.event.EventActions;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.consumer.utils.CacheUri;
import no.fint.event.model.Event;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.relation.FintResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Slf4j
@Service
public class PersonalressursCacheService extends CacheService<FintResource<Personalressurs>> {

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Value("${fint.events.orgs:mock.no}")
    private String[] orgs;

    @PostConstruct
    public void init() {
        Arrays.stream(orgs).forEach(orgId -> {
            FintCache<FintResource<Personalressurs>> cache = new FintCache<>();
            String cacheUri = CacheUri.create(orgId, "personalressurs");
            caches.put(cacheUri, cache);
        });
    }

    @Scheduled(initialDelayString = "${fint.consumer.cache.initialDelay.staffresource:30000}", fixedRateString = "${fint.consumer.cache.fixedRate.staffresource:55000}")
    public void getAllStaffResources() {
        Arrays.stream(orgs).forEach(orgId -> {
            log.info("Populating employee cache for {}", orgId);
            Event event = new Event(orgId, "administrasjon/personal", EventActions.GET_ALL_PERSONALRESSURS.name(), "CACHE_SERVICE");
            consumerEventUtil.send(event);
        });
    }

}
