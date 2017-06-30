package no.fint.consumer.person;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheService;
import no.fint.cache.FintCache;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.model.administrasjon.personal.PersonalActions;
import no.fint.model.felles.Person;
import no.fint.model.relation.FintResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Slf4j
@Service
public class PersonCacheService extends CacheService<FintResource<Person>> {

    public static final String MODEL = Person.class.getSimpleName().toLowerCase();

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    public PersonCacheService() {
        super(MODEL);
    }

    @PostConstruct
    public void init() {
        Arrays.stream(props.getOrgs()).forEach(orgId -> {
            FintCache<FintResource<Person>> cache = new FintCache<>();
            put(orgId, cache);
        });
    }

    @Scheduled(initialDelayString = ConsumerProps.CACHE_INITIALDELAY_PERSON, fixedRateString = ConsumerProps.CACHE_FIXEDRATE_PERSON)
    public void getAllPersons() {
        Arrays.stream(props.getOrgs()).forEach(orgId -> {
            log.info("Populating person cache for {}", orgId);
            Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_PERSON, Constants.CACHE_SERIVCE);
            consumerEventUtil.send(event);
        });
    }

}
