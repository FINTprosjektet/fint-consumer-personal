package no.fint.consumer.personalressurs;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.FintCache;
import no.fint.consumer.CacheService;
import no.fint.consumer.event.EventUtil;
import no.fint.consumer.utils.CacheUri;
import no.fint.event.model.Event;
import no.fint.model.administrasjon.personal.Personalressurs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class PersonalressursCacheService extends CacheService<Personalressurs> {

    @Autowired
    private EventUtil eventUtil;

    @PostConstruct
    public void init() {
        FintCache<Personalressurs> cache = new FintCache<>();
        String cacheUri = CacheUri.create("mock.no", "personalressurs");
        caches.put(cacheUri, cache);
    }

    @Scheduled(initialDelay = 30000L, fixedRate = 55000L)
    public void getAllStaffResources() {
        String orgId = "mock.no";
        log.info("Populating employee cache for {}", orgId);
        Event event = new Event(orgId, "administrasjon/personal", "GET_ALL_EMPLOYEES", "CACHE_SERVICE");
        eventUtil.send(event);
    }

}
