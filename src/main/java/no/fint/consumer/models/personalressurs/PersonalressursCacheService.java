package no.fint.consumer.models.personalressurs;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

import no.fint.cache.CacheService;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.relations.FintResourceCompatibility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.administrasjon.personal.PersonalActions;

@Slf4j
@Service
public class PersonalressursCacheService extends CacheService<PersonalressursResource> {

    public static final String MODEL = Personalressurs.class.getSimpleName().toLowerCase();

    @Value("${fint.consumer.compatibility.fintresource:true}")
    private boolean checkFintResourceCompatibility;

    @Autowired
    private FintResourceCompatibility fintResourceCompatibility;

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    @Autowired
    private PersonalressursLinker linker;

    private JavaType javaType;

    private ObjectMapper objectMapper;

    public PersonalressursCacheService() {
        super(MODEL, PersonalActions.GET_ALL_PERSONALRESSURS, PersonalActions.UPDATE_PERSONALRESSURS);
        objectMapper = new ObjectMapper();
        javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, PersonalressursResource.class);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @PostConstruct
    public void init() {
        props.getAssets().forEach(this::createCache);
    }

    @Scheduled(initialDelayString = Constants.CACHE_INITIALDELAY_PERSONALRESSURS, fixedRateString = Constants.CACHE_FIXEDRATE_PERSONALRESSURS)
    public void populateCacheAll() {
        props.getAssets().forEach(this::populateCache);
    }

    public void rebuildCache(String orgId) {
		flush(orgId);
		populateCache(orgId);
	}

    @Override
    public void populateCache(String orgId) {
		log.info("Populating Personalressurs cache for {}", orgId);
        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_PERSONALRESSURS, Constants.CACHE_SERVICE);
        consumerEventUtil.send(event);
    }


    public Optional<PersonalressursResource> getPersonalressursByAnsattnummer(String orgId, String ansattnummer) {
        return getOne(orgId, (resource) -> Optional
                .ofNullable(resource)
                .map(PersonalressursResource::getAnsattnummer)
                .map(Identifikator::getIdentifikatorverdi)
                .map(_id -> _id.equals(ansattnummer))
                .orElse(false));
    }

    public Optional<PersonalressursResource> getPersonalressursByBrukernavn(String orgId, String brukernavn) {
        return getOne(orgId, (resource) -> Optional
                .ofNullable(resource)
                .map(PersonalressursResource::getBrukernavn)
                .map(Identifikator::getIdentifikatorverdi)
                .map(_id -> _id.equals(brukernavn))
                .orElse(false));
    }

    public Optional<PersonalressursResource> getPersonalressursBySystemId(String orgId, String systemId) {
        return getOne(orgId, (resource) -> Optional
                .ofNullable(resource)
                .map(PersonalressursResource::getSystemId)
                .map(Identifikator::getIdentifikatorverdi)
                .map(_id -> _id.equals(systemId))
                .orElse(false));
    }


	@Override
    public void onAction(Event event) {
        List<PersonalressursResource> data;
        if (checkFintResourceCompatibility && fintResourceCompatibility.isFintResourceData(event.getData())) {
            log.info("Compatibility: Converting FintResource<PersonalressursResource> to PersonalressursResource ...");
            data = fintResourceCompatibility.convertResourceData(event.getData(), PersonalressursResource.class);
        } else {
            data = objectMapper.convertValue(event.getData(), javaType);
        }
        data.forEach(linker::mapLinks);
        if (PersonalActions.valueOf(event.getAction()) == PersonalActions.UPDATE_PERSONALRESSURS) {
            if (event.getResponseStatus() == ResponseStatus.ACCEPTED || event.getResponseStatus() == ResponseStatus.CONFLICT) {
                add(event.getOrgId(), data);
                log.info("Added {} elements to cache for {}", data.size(), event.getOrgId());
            } else {
                log.debug("Ignoring payload for {} with response status {}", event.getOrgId(), event.getResponseStatus());
            }
        } else {
            update(event.getOrgId(), data);
            log.info("Updated cache for {} with {} elements", event.getOrgId(), data.size());
        }
    }
}
