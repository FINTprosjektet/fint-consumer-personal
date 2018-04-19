package no.fint.consumer.models.person;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

import no.fint.cache.CacheService;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.relations.FintResourceCompatibility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import no.fint.model.felles.Person;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.felles.FellesActions;

@Slf4j
@Service
public class PersonCacheService extends CacheService<PersonResource> {

    public static final String MODEL = Person.class.getSimpleName().toLowerCase();

    @Value("${fint.consumer.compatibility.fintresource:true}")
    private boolean checkFintResourceCompatibility;

    @Autowired
    private FintResourceCompatibility fintResourceCompatibility;

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    @Autowired
    private PersonLinker linker;

    private JavaType javaType;

    private ObjectMapper objectMapper;

    public PersonCacheService() {
        super(MODEL, FellesActions.GET_ALL_PERSON, FellesActions.UPDATE_PERSON);
        objectMapper = new ObjectMapper();
        javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, PersonResource.class);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
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
		log.info("Populating Person cache for {}", orgId);
        Event event = new Event(orgId, Constants.COMPONENT, FellesActions.GET_ALL_PERSON, Constants.CACHE_SERVICE);
        consumerEventUtil.send(event);
    }


    public Optional<PersonResource> getPersonByFodselsnummer(String orgId, String fodselsnummer) {
        return getOne(orgId, (resource) -> Optional
                .ofNullable(resource)
                .map(PersonResource::getFodselsnummer)
                .map(Identifikator::getIdentifikatorverdi)
                .map(_id -> _id.equals(fodselsnummer))
                .orElse(false));
    }


	@Override
    public void onAction(Event event) {
        List<PersonResource> data;
        if (checkFintResourceCompatibility && fintResourceCompatibility.isFintResourceData(event.getData())) {
            log.info("Compatibility: Converting FintResource<PersonResource> to PersonResource ...");
            data = fintResourceCompatibility.convertResourceData(event.getData(), PersonResource.class);
        } else {
            data = objectMapper.convertValue(event.getData(), javaType);
        }
        data.forEach(linker::toResource);
        if (FellesActions.valueOf(event.getAction()) == FellesActions.UPDATE_PERSON) {
            add(event.getOrgId(), data);
            log.info("Added {} elements to cache for {}", data.size(), event.getOrgId());
        } else {
            update(event.getOrgId(), data);
            log.info("Updated cache for {} with {} elements", event.getOrgId(), data.size());
        }
    }
}
