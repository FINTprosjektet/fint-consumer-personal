package no.fint.consumer.person;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheService;
import no.fint.cache.FintCache;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.model.felles.FellesActions;
import no.fint.model.felles.Person;
import no.fint.model.relation.FintResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PersonCacheService extends CacheService<FintResource<Person>> {

    public static final String MODEL = Person.class.getSimpleName().toLowerCase();

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    public PersonCacheService() {
        super(MODEL, FellesActions.GET_ALL_PERSON);
    }

    @PostConstruct
    public void init() {
        Arrays.stream(props.getOrgs()).forEach(this::createCache);
    }

    @Scheduled(initialDelayString = ConsumerProps.CACHE_INITIALDELAY_PERSON, fixedRateString = ConsumerProps.CACHE_FIXEDRATE_PERSON)
    public void populateCacheAll() {
        Arrays.stream(props.getOrgs()).forEach(this::populateCache);
    }

    public void rebuildCache(String orgId) {
        flush(orgId);
        populateCache(orgId);
    }

    private void populateCache(String orgId) {
        log.info("Populating person cache for {}", orgId);
        Event event = new Event(orgId, Constants.COMPONENT, FellesActions.GET_ALL_PERSON, Constants.CACHE_SERVICE);
        consumerEventUtil.send(event);
    }

    public Optional<FintResource<Person>> getPerson(String orgId, String fodselsnummer) {
        return getOne(orgId, (fintResource) -> fintResource.getResource().getFodselsnummer().getIdentifikatorverdi().equals(fodselsnummer));
    }

    @Override
    public void onAction(Event event) {
        update(event, new TypeReference<List<FintResource<Person>>>() {
        });
    }
}
