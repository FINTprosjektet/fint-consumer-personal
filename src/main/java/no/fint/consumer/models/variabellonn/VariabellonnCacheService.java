package no.fint.consumer.models.variabellonn;

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

import no.fint.model.administrasjon.personal.Variabellonn;
import no.fint.model.administrasjon.personal.PersonalActions;

@Slf4j
@Service
public class VariabellonnCacheService extends CacheService<FintResource<Variabellonn>> {

    public static final String MODEL = Variabellonn.class.getSimpleName().toLowerCase();

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    public VariabellonnCacheService() {
        super(MODEL, PersonalActions.GET_ALL_VARIABELLONN);
    }

    @PostConstruct
    public void init() {
        Arrays.stream(props.getOrgs()).forEach(this::createCache);
    }

    @Scheduled(initialDelayString = ConsumerProps.CACHE_INITIALDELAY_VARIABELLONN, fixedRateString = ConsumerProps.CACHE_FIXEDRATE_VARIABELLONN)
    public void populateCacheAll() {
        Arrays.stream(props.getOrgs()).forEach(this::populateCache);
    }

    public void rebuildCache(String orgId) {
		flush(orgId);
		populateCache(orgId);
	}

    private void populateCache(String orgId) {
		log.info("Populating Variabellonn cache for {}", orgId);
        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_VARIABELLONN, Constants.CACHE_SERVICE);
        consumerEventUtil.send(event);
    }


    public Optional<FintResource<Variabellonn>> getVariabellonnBySystemId(String orgId, String systemId) {
        Identifikator needle = new Identifikator();
        needle.setIdentifikatorverdi(systemId);
        return getOne(orgId, (fintResource) -> needle.equals(fintResource.getResource().getSystemId()));
    }


	@Override
    public void onAction(Event event) {
        update(event, new TypeReference<List<FintResource<Variabellonn>>>() {
        });
    }
}
