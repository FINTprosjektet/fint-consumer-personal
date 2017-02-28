package no.fint.consumer.arbeidsforhold;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.FintCache;
import no.fint.consumer.CacheService;
import no.fint.consumer.event.EventUtil;
import no.fint.consumer.utils.CacheUri;
import no.fint.event.model.Event;
import no.fint.personal.Arbeidsforhold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ArbeidsforholdCacheService extends CacheService<Arbeidsforhold> {

    @Autowired
    private EventUtil eventUtil;

    @PostConstruct
    public void init() {
        FintCache<Arbeidsforhold> cache = new FintCache<>();
        String cacheUri = CacheUri.create("mock.no", "arbeidsforhold");
        caches.put(cacheUri, cache);
    }

    @Scheduled(initialDelay = 50000L, fixedRate = 55000L)
    public void getAllEmployments() {
        String orgId = "mock.no";
        log.info("Populating employment cache for {}", orgId);
        Event event = new Event(orgId, "administrasjon/personal", "GET_ALL_EMPLOYMENTS", "CACHE_SERVICE");
        eventUtil.send(event);
    }
}
