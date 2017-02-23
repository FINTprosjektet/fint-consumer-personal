package no.fint.consumer.service;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.Cache;
import no.fint.cache.FintCache;
import no.fint.cache.model.CacheObject;
import no.fint.consumer.test.TestObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CacheService {
    private Map<String, Cache<TestObject>> caches;

    @PostConstruct
    public void init() {
        FintCache<TestObject> cache = new FintCache<>();
        cache.update(Lists.newArrayList(new TestObject("test1"), new TestObject("test2")));

        caches = new HashMap<>();
        caches.put("rogfk.no", cache);
    }

    public long getLastUpdated(String orgId) {
        FintCache fintCache = (FintCache) caches.get(orgId);
        return fintCache.getLastUpdated();
    }

    private Optional<Cache<TestObject>> getCache(String orgId) {
        if (orgId == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(caches.get(orgId));
    }

    public List<TestObject> getAll(String orgId) {
        Optional<Cache<TestObject>> cache = getCache(orgId);
        if (cache.isPresent()) {
            List<CacheObject<TestObject>> cacheObjects = cache.get().get();
            return cacheObjects.stream().map(CacheObject::getObject).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<TestObject> getAll(String orgId, long sinceTimestamp) {
        Optional<Cache<TestObject>> cache = getCache(orgId);
        if (cache.isPresent()) {
            List<CacheObject<TestObject>> cacheObjects = cache.get().getSince(sinceTimestamp);
            return cacheObjects.stream().map(CacheObject::getObject).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public void update(String orgId, List<TestObject> testObjects) {
        Optional<Cache<TestObject>> cache = getCache(orgId);
        cache.ifPresent(testObjectCache -> testObjectCache.update(testObjects));
    }
}
