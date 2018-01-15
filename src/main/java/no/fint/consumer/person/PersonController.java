package no.fint.consumer.person;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.consumer.config.Constants;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.Event;
import no.fint.event.model.HeaderConstants;
import no.fint.event.model.Status;
import no.fint.model.felles.FellesActions;
import no.fint.model.felles.Person;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintRelationsMediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = RestEndpoints.PERSON, produces = {FintRelationsMediaType.APPLICATION_HAL_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
public class PersonController {

    @Autowired
    private FintAuditService fintAuditService;

    @Autowired
    private PersonCacheService cacheService;

    @Autowired
    private PersonAssembler assembler;

    @GetMapping("/last-updated")
    public Map<String, String> getLastUpdated(@RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId) {
        String lastUpdated = Long.toString(cacheService.getLastUpdated(orgId));
        return ImmutableMap.of("lastUpdated", lastUpdated);
    }

    @GetMapping("/cache/size")
    public ImmutableMap<String, Integer> getCacheSize(@RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId) {
        return ImmutableMap.of("size", cacheService.getAll(orgId).size());
    }

    @PostMapping("/cache/rebuild")
    public void rebuildCache(@RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId) {
        cacheService.rebuildCache(orgId);
    }

    @GetMapping
    public ResponseEntity getAllPersoner(@RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId,
                                         @RequestHeader(value = HeaderConstants.CLIENT, defaultValue = Constants.DEFAULT_HEADER_CLIENT) String client,
                                         @RequestParam(required = false) Long sinceTimeStamp) {
        log.info("OrgId: {}, Client: {}", orgId, client);
        log.info("SinceTimeStamp: {}", sinceTimeStamp);

        Event event = new Event(orgId, Constants.COMPONENT, FellesActions.GET_ALL_PERSON, client);
        fintAuditService.audit(event);

        fintAuditService.audit(event, Status.CACHE);

        List<FintResource<Person>> personer;
        if (sinceTimeStamp == null) {
            personer = cacheService.getAll(orgId);
        } else {
            personer = cacheService.getAll(orgId, sinceTimeStamp);
        }

        fintAuditService.audit(event, Status.CACHE_RESPONSE, Status.SENT_TO_CLIENT);

        return assembler.resources(personer);
    }

    @GetMapping("/fodselsnummer/{id}")
    public ResponseEntity getPerson(@PathVariable String id,
                                    @RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId,
                                    @RequestHeader(value = HeaderConstants.CLIENT, defaultValue = Constants.DEFAULT_HEADER_CLIENT) String client) {
        log.info("Id: {}, OrgId: {}, Client: {}", id.substring(0,8), orgId, client);

        Event event = new Event(orgId, Constants.COMPONENT, FellesActions.GET_PERSON, client);
        fintAuditService.audit(event);

        fintAuditService.audit(event, Status.CACHE);

        Optional<FintResource<Person>> personOptional = cacheService.getPerson(orgId, id);

        fintAuditService.audit(event, Status.CACHE_RESPONSE, Status.SENT_TO_CLIENT);

        if (personOptional.isPresent()) {
            return assembler.resource(personOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
