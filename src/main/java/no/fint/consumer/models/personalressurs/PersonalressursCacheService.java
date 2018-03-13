package no.fint.consumer.models.personalressurs;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheService;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.model.relation.FintResource;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.administrasjon.personal.PersonalActions;

@Slf4j
@Service
public class PersonalressursCacheService extends CacheService<FintResource<Personalressurs>> {

    public static final String MODEL = Personalressurs.class.getSimpleName().toLowerCase();

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    public PersonalressursCacheService() {
        super(MODEL, PersonalActions.GET_ALL_PERSONALRESSURS);
    }

    @PostConstruct
    public void init() {
        Arrays.stream(props.getOrgs()).forEach(this::createCache);
    }

    @Scheduled(initialDelayString = ConsumerProps.CACHE_INITIALDELAY_PERSONALRESSURS, fixedRateString = ConsumerProps.CACHE_FIXEDRATE_PERSONALRESSURS)
    public void populateCacheAll() {
        Arrays.stream(props.getOrgs()).forEach(this::populateCache);
    }

    public void rebuildCache(String orgId) {
		flush(orgId);
		populateCache(orgId);
	}

    private void populateCache(String orgId) {
		log.info("Populating Personalressurs cache for {}", orgId);
        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_PERSONALRESSURS, Constants.CACHE_SERVICE);
        consumerEventUtil.send(event);
    }


    public Optional<FintResource<Personalressurs>> getPersonalressursByAnsattnummer(String orgId, String ansattnummer) {
        return getOne(orgId, (fintResource) -> Optional
                .ofNullable(fintResource)
                .map(FintResource::getResource)
                .map(Personalressurs::getAnsattnummer)
                .map(Identifikator::getIdentifikatorverdi)
                .map(id -> id.equals(ansattnummer))
                .orElse(false));
    }

    public Optional<FintResource<Personalressurs>> getPersonalressursByBrukernavn(String orgId, String brukernavn) {
        return getOne(orgId, (fintResource) -> Optional
                .ofNullable(fintResource)
                .map(FintResource::getResource)
                .map(Personalressurs::getBrukernavn)
                .map(Identifikator::getIdentifikatorverdi)
                .map(id -> id.equals(brukernavn))
                .orElse(false));
    }

    public Optional<FintResource<Personalressurs>> getPersonalressursBySystemId(String orgId, String systemId) {
        return getOne(orgId, (fintResource) -> Optional
                .ofNullable(fintResource)
                .map(FintResource::getResource)
                .map(Personalressurs::getSystemId)
                .map(Identifikator::getIdentifikatorverdi)
                .map(id -> id.equals(systemId))
                .orElse(false));
    }


	@Override
    public void onAction(Event event) {
        update(event, new TypeReference<List<FintResource<Personalressurs>>>() {
        });
    }
}
