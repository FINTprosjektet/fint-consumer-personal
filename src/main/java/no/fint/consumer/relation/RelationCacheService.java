package no.fint.consumer.relation;

import com.google.common.collect.Lists;
import no.fint.cache.FintCache;
import no.fint.consumer.CacheService;
import no.fint.consumer.utils.CacheUri;
import no.fint.relation.model.Relation;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RelationCacheService extends CacheService<Relation> {

    @PostConstruct
    public void init() {
        FintCache<Relation> cache = new FintCache<>();

        String cacheUri = CacheUri.create("rogfk.no", "relations");
        caches.put(cacheUri, cache);
        super.update(cacheUri, Lists.newArrayList(new Relation()));
    }
}
