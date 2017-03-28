package no.fint.consumer.relation;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.Cache;
import no.fint.cache.FintCache;
import no.fint.cache.model.CacheObject;
import no.fint.consumer.CacheService;
import no.fint.consumer.event.EventUtil;
import no.fint.consumer.utils.CacheUri;
import no.fint.event.model.Event;
import no.fint.model.relation.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RelationCacheService extends CacheService<Relation> {

    @Autowired
    private EventUtil eventUtil;

    @Value("${fint.events.orgs:mock.no}")
    private String[] orgs;

    @PostConstruct
    public void init() {
        Arrays.stream(orgs).forEach(orgId -> {
            FintCache<Relation> cache = new FintCache<>();
            String cacheUri = CacheUri.create(orgId, "relation");
            caches.put(cacheUri, cache);
        });
    }

    @Scheduled(initialDelayString = "${fint.consumer.cache.initialDelay.relation:20000}", fixedRateString = "${fint.consumer.cache.fixedRate.relation:55000}")
    public void getAllRelations() {
        Arrays.stream(orgs).forEach(orgId -> {
            log.info("Populating relation cache for {}", orgId);
            Event event = new Event(orgId, "administrasjon/personal", "GET_RELATIONS", "CACHE_SERVICE");
            eventUtil.send(event);
        });
    }

    public List<String> getKey(String type, String main) {
        String orgId = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("x-org-id");
        Cache<Relation> cache = caches.get(CacheUri.create(orgId, "relation"));
        List<CacheObject<Relation>> cacheObjects = cache.get();
        return cacheObjects.stream().filter(cacheObject -> isRelation(type, main, cacheObject))
                .map(cacheObject -> cacheObject.getObject().getRelated()).collect(Collectors.toList());
    }

    private boolean isRelation(String type, String main, CacheObject<Relation> cacheObject) {
        return cacheObject.getObject().getType().equals(type) && cacheObject.getObject().getMain().equals(main);
    }

}
