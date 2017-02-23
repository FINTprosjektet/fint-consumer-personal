package no.fint.consumer.personalressurs;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.Cache;
import no.fint.cache.FintCache;
import no.fint.cache.model.CacheObject;
import no.fint.consumer.test.TestObject;
import no.fint.consumer.utils.CacheUri;
import no.fint.personal.Personalressurs;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonalressursCacheService {
    private Map<String, Cache<Personalressurs>> caches;

    @PostConstruct
    public void init() {
        FintCache<Personalressurs> cache = new FintCache<>();
        cache.update(Lists.newArrayList(new Personalressurs()));

        caches = new HashMap<>();
        caches.put(CacheUri.create("rogfk.no", "personalressurs"), cache);
    }

    public long getLastUpdated(String orgId) {
        FintCache fintCache = (FintCache) caches.get(orgId);
        return fintCache.getLastUpdated();
    }

    private Optional<Cache<Personalressurs>> getCache(String cacheUri) {
        if (cacheUri == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(caches.get(cacheUri));
    }

    public List<Personalressurs> getAll(String cacheUri) {
        Optional<Cache<Personalressurs>> cache = getCache(cacheUri);
        if (cache.isPresent()) {
            List<CacheObject<Personalressurs>> cacheObjects = cache.get().get();
            return cacheObjects.stream().map(CacheObject::getObject).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<Personalressurs> getAll(String cacheUri, long sinceTimestamp) {
        Optional<Cache<Personalressurs>> cache = getCache(cacheUri);
        if (cache.isPresent()) {
            List<CacheObject<Personalressurs>> cacheObjects = cache.get().getSince(sinceTimestamp);
            return cacheObjects.stream().map(CacheObject::getObject).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public void update(String cacheUri, List<Personalressurs> testObjects) {
        Optional<Cache<Personalressurs>> cache = getCache(cacheUri);
        cache.ifPresent(testObjectCache -> testObjectCache.update(testObjects));
    }
}
