package no.fint.consumer.personalressurs;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheService;
import no.fint.cache.FintCache;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.model.administrasjon.personal.PersonalActions;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.relation.FintResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class PersonalressursCacheService extends CacheService<FintResource<Personalressurs>> {

    public static final String MODEL = Personalressurs.class.getSimpleName().toLowerCase();

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    public PersonalressursCacheService() {
        super(MODEL);
    }

    @PostConstruct
    public void init() {
        Arrays.stream(props.getOrgs()).forEach(orgId -> {
            FintCache<FintResource<Personalressurs>> cache = new FintCache<>();
            put(orgId, cache);
        });
    }

    @Scheduled(initialDelayString = ConsumerProps.CACHE_INITIALDELAY_PERSONALRESSURS, fixedRateString = ConsumerProps.CACHE_FIXEDRATE_PERSONALRESSURS)
    public void populateCacheAllPersonalressurs() {
        Arrays.stream(props.getOrgs()).forEach(orgId -> {
            log.info("Populating employee cache for {}", orgId);
            Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_PERSONALRESSURS, Constants.CACHE_SERIVCE);
            consumerEventUtil.send(event);
        });
    }

    public Optional<FintResource<Personalressurs>> getPersonalressurs(String orgId, String ansattnummer) {
        return getOne(orgId, (fintResource) -> fintResource.getResource().getAnsattnummer().getIdentifikatorverdi().equals(ansattnummer));
    }
}
