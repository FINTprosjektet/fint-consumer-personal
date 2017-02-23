package no.fint.consumer.person;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.Cache;
import no.fint.cache.FintCache;
import no.fint.cache.model.CacheObject;
import no.fint.consumer.utils.CacheUri;
import no.fint.felles.Person;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonCacheService {
    private Map<String, Cache<Person>> caches;

    @PostConstruct
    public void init() {
        FintCache<Person> cache = new FintCache<>();
        cache.update(Lists.newArrayList(new Person()));

        caches = new HashMap<>();
        caches.put(CacheUri.create("rogfk.no", "person"), cache);
    }

    public long getLastUpdated(String orgId) {
        FintCache fintCache = (FintCache) caches.get(orgId);
        return fintCache.getLastUpdated();
    }

    private Optional<Cache<Person>> getCache(String cacheUri) {
        if (cacheUri == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(caches.get(cacheUri));
    }

    public List<Person> getAll(String cacheUri) {
        Optional<Cache<Person>> cache = getCache(cacheUri);
        if (cache.isPresent()) {
            List<CacheObject<Person>> cacheObjects = cache.get().get();
            return cacheObjects.stream().map(CacheObject::getObject).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<Person> getAll(String cacheUri, long sinceTimestamp) {
        Optional<Cache<Person>> cache = getCache(cacheUri);
        if (cache.isPresent()) {
            List<CacheObject<Person>> cacheObjects = cache.get().getSince(sinceTimestamp);
            return cacheObjects.stream().map(CacheObject::getObject).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public void update(String cacheUri, List<Person> testObjects) {
        Optional<Cache<Person>> cache = getCache(cacheUri);
        cache.ifPresent(testObjectCache -> testObjectCache.update(testObjects));
    }
}
