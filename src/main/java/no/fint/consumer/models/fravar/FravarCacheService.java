package no.fint.consumer.models.fravar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

import no.fint.cache.CacheService;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.relation.FintResource;
import no.fint.model.resource.Link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import no.fint.model.administrasjon.personal.Fravar;
import no.fint.model.resource.administrasjon.personal.FravarResource;
import no.fint.model.administrasjon.personal.PersonalActions;

@Slf4j
@Service
public class FravarCacheService extends CacheService<FravarResource> {

    public static final String MODEL = Fravar.class.getSimpleName().toLowerCase();

    @Value("${fint.consumer.compatibility.fintresource:true}")
    private boolean fintResourceCompatibility;

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    public FravarCacheService() {
        super(MODEL, PersonalActions.GET_ALL_FRAVAR);
    }

    @PostConstruct
    public void init() {
        Arrays.stream(props.getOrgs()).forEach(this::createCache);
    }

    @Scheduled(initialDelayString = ConsumerProps.CACHE_INITIALDELAY_FRAVAR, fixedRateString = ConsumerProps.CACHE_FIXEDRATE_FRAVAR)
    public void populateCacheAll() {
        Arrays.stream(props.getOrgs()).forEach(this::populateCache);
    }

    public void rebuildCache(String orgId) {
		flush(orgId);
		populateCache(orgId);
	}

    private void populateCache(String orgId) {
		log.info("Populating Fravar cache for {}", orgId);
        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_FRAVAR, Constants.CACHE_SERVICE);
        consumerEventUtil.send(event);
    }


    public Optional<FravarResource> getFravarBySystemId(String orgId, String systemId) {
        return getOne(orgId, (resource) -> Optional
                .ofNullable(resource)
                .map(FravarResource::getSystemId)
                .map(Identifikator::getIdentifikatorverdi)
                .map(_id -> _id.equals(systemId))
                .orElse(false));
    }


	@Override
    public void onAction(Event event) {
        if (fintResourceCompatibility && !event.getData().isEmpty() && event.getData().get(0) instanceof FintResource) {
            log.info("Compatibility: Converting FintResource<FravarResource> to FravarResource ...");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            List<FintResource<FravarResource>> original = objectMapper.convertValue(event.getData(), new TypeReference<List<FintResource<FravarResource>>>() {
            });
            List<FravarResource> replacement = original.stream().map(fintResource -> {
                FravarResource resource = fintResource.getResource();
                fintResource.getRelations().forEach(relation -> resource.addLink(relation.getRelationName(), Link.with(relation.getLink())));
                return resource;
            }).collect(Collectors.toList());
            event.setData(replacement);
        }
        update(event, new TypeReference<List<FravarResource>>() {
        });
    }
}
