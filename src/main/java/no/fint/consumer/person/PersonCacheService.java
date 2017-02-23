package no.fint.consumer.person;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.FintCache;
import no.fint.consumer.CacheService;
import no.fint.consumer.utils.CacheUri;
import no.fint.felles.Person;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class PersonCacheService extends CacheService<Person> {

    @PostConstruct
    public void init() {
        FintCache<Person> cache = new FintCache<>();
        cache.update(Lists.newArrayList(new Person()));

        String cacheUri = CacheUri.create("rogfk.no", "person");
        caches.put(cacheUri, cache);
        super.update(cacheUri, Lists.newArrayList());
    }

}
