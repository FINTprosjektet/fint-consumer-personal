package no.fint.consumer.arbeidsforhold;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.FintCache;
import no.fint.consumer.CacheService;
import no.fint.consumer.event.EventUtil;
import no.fint.consumer.utils.CacheUri;
import no.fint.event.model.Event;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.relation.FintResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Slf4j
@Service
public class ArbeidsforholdCacheService extends CacheService<FintResource<Arbeidsforhold>> {

    @Autowired
    private EventUtil eventUtil;

    @Value("${fint.events.orgs:mock.no}")
    private String[] orgs;

    @PostConstruct
    public void init() {
        Arrays.stream(orgs).forEach(orgId -> {
            FintCache<FintResource<Arbeidsforhold>> cache = new FintCache<>();
            String cacheUri = CacheUri.create(orgId, "arbeidsforhold");
            caches.put(cacheUri, cache);
        });
    }

    @Scheduled(initialDelayString = "${fint.consumer.cache.initialDelay.employment:50000}", fixedRateString = "${fint.consumer.cache.fixedRate.employment:55000}")
    public void getAllEmployments() {
        Arrays.stream(orgs).forEach(orgId -> {
            log.info("Populating employment cache for {}", orgId);
            Event event = new Event(orgId, "administrasjon/personal", "GET_ALL_ARBEIDSFORHOLD", "CACHE_SERVICE");
            eventUtil.send(event);
        });
    }
}
