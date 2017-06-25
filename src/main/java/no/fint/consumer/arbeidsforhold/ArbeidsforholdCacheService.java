package no.fint.consumer.arbeidsforhold;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheService;
import no.fint.cache.FintCache;
import no.fint.cache.utils.CacheUri;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.PersonalActions;
import no.fint.model.relation.FintResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Slf4j
@Service
public class ArbeidsforholdCacheService extends CacheService<FintResource<Arbeidsforhold>> {

    public static final String MODEL = Arbeidsforhold.class.getSimpleName().toLowerCase();

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    @PostConstruct
    public void init() {
        Arrays.stream(props.getOrgs()).forEach(orgId -> {
            FintCache<FintResource<Arbeidsforhold>> cache = new FintCache<>();
            String cacheUri = CacheUri.create(orgId, MODEL);
            put(cacheUri, cache);
        });
    }

    @Scheduled(initialDelayString = ConsumerProps.CACHE_INITIALDELAY_ARBEIDSFORHOLD, fixedRateString = ConsumerProps.CACHE_FIXEDRATE_ARBEIDSFORHOLD)
    public void populateCacheAllArbeidsforhold() {
        Arrays.stream(props.getOrgs()).forEach(orgId -> {
            log.info("Populating arbeidsforhold cache for {}", orgId);
            Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_ARBEIDSFORHOLD, Constants.CACHE_SERIVCE);
            consumerEventUtil.send(event);
        });
    }
}
