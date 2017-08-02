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
import no.fint.relations.annotations.FintRelations;
import no.fint.relations.annotations.FintSelf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@FintSelf(type = Person.class, property = "fodselsnummer")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = RestEndpoints.PERSON, produces = {"application/hal+json", MediaType.APPLICATION_JSON_UTF8_VALUE})
public class PersonController {

    @Autowired
    private FintAuditService fintAuditService;

    @Autowired
    private PersonCacheService cacheService;

    @RequestMapping(value = "/last-updated", method = RequestMethod.GET)
    public Map<String, String> getLastUpdated(@RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId) {
        String lastUpdated = Long.toString(cacheService.getLastUpdated(orgId));
        return ImmutableMap.of("lastUpdated", lastUpdated);
    }

    @FintRelations
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllPersoner(@RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId,
                                         @RequestHeader(value = HeaderConstants.CLIENT, defaultValue = Constants.DEFAULT_HEADER_CLIENT) String client,
                                         @RequestParam(required = false) Long sinceTimeStamp) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);
        log.info("SinceTimeStamp: {}", sinceTimeStamp);

        Event event = new Event(orgId, Constants.COMPONENT, FellesActions.GET_ALL_PERSON, client);
        fintAuditService.audit(event);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event);

        List<FintResource<Person>> personer;
        if (sinceTimeStamp == null) {
            personer = cacheService.getAll(orgId);
        } else {
            personer = cacheService.getAll(orgId, sinceTimeStamp);
        }

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event);

        return ResponseEntity.ok(personer);
    }

    @FintRelations
    @RequestMapping(value = "/fodselsnummer/{id}", method = RequestMethod.GET)
    public ResponseEntity getPerson(@PathVariable String id,
                                    @RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId,
                                    @RequestHeader(value = HeaderConstants.CLIENT, defaultValue = Constants.DEFAULT_HEADER_CLIENT) String client) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);

        Event event = new Event(orgId, Constants.COMPONENT, FellesActions.GET_PERSON, client);
        fintAuditService.audit(event);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event);

        List<FintResource<Person>> personer = cacheService.getAll(orgId);

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event);

        Optional<FintResource<Person>> personOptional = personer.stream().filter(
                person -> person.getConvertedResource().getFodselsnummer().getIdentifikatorverdi().equals(id)
        ).findFirst();

        if (personOptional.isPresent()) {
            return ResponseEntity.ok(personOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
