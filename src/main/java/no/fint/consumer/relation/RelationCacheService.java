package no.fint.consumer.relation;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.Cache;
import no.fint.cache.FintCache;
import no.fint.cache.model.CacheObject;
import no.fint.consumer.CacheService;
import no.fint.consumer.event.EventUtil;
import no.fint.consumer.utils.CacheUri;
import no.fint.event.model.Event;
import no.fint.relation.model.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RelationCacheService extends CacheService<Relation> {

    @Autowired
    private EventUtil eventUtil;

    @PostConstruct
    public void init() {
        FintCache<Relation> cache = new FintCache<>();
        String cacheUri = CacheUri.create("mock.no", "relation");
        caches.put(cacheUri, cache);
    }

    @Scheduled(initialDelay = 20000L, fixedRate = 55000L)
    public void getAllEmployments() {
        String orgId = "mock.no";
        log.info("Populating relation cache for {}", orgId);
        Event event = new Event(orgId, "administrasjon/personal", "GET_RELATIONS", "CACHE_SERVICE");
        eventUtil.send(event);
    }

    public List<String> getKey(String type, String leftKey) {
        Cache<Relation> cache = caches.get(CacheUri.create("mock.no", "relation"));
        List<CacheObject<Relation>> cacheObjects = cache.get();
        return cacheObjects.stream().filter(cacheObject -> isRelation(type, leftKey, cacheObject))
                .map(cacheObject -> cacheObject.getObject().getRightKey()).collect(Collectors.toList());
    }

    private boolean isRelation(String type, String leftKey, CacheObject<Relation> cacheObject) {
        return cacheObject.getObject().getType().equals(type) && cacheObject.getObject().getLeftKey().equals(leftKey);
    }

}
