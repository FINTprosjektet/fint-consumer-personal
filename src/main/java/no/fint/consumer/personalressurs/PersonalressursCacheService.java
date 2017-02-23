package no.fint.consumer.personalressurs;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.FintCache;
import no.fint.consumer.CacheService;
import no.fint.consumer.utils.CacheUri;
import no.fint.personal.Personalressurs;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class PersonalressursCacheService extends CacheService<Personalressurs> {
    @PostConstruct
    public void init() {
        FintCache<Personalressurs> cache = new FintCache<>();
        cache.update(Lists.newArrayList(new Personalressurs()));

        String cacheUri = CacheUri.create("rogfk.no", "personalressurs");
        caches.put(cacheUri, cache);
        super.update(cacheUri, Lists.newArrayList(new Personalressurs()));
    }

}
