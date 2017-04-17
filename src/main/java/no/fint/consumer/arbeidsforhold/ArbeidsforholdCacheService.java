package no.fint.consumer.arbeidsforhold;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.FintCache;
import no.fint.consumer.CacheService;
import no.fint.consumer.event.ConsumerEventUtil;
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
    private ConsumerEventUtil consumerEventUtil;

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

    @Scheduled(initialDelayString = "${fint.consumer.cache.initialDelay.arbeidsforhold:50000}", fixedRateString = "${fint.consumer.cache.fixedRate.arbeidsforhold:55000}")
    public void populateCacheAllArbeidsforhold() {
        Arrays.stream(orgs).forEach(orgId -> {
            log.info("Populating arbeidsforhold cache for {}", orgId);
            Event event = new Event(orgId, "administrasjon/personal", "GET_ALL_ARBEIDSFORHOLD", "CACHE_SERVICE");
            consumerEventUtil.send(event);
        });
    }
}
