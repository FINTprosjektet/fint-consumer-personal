package no.fint.consumer.person;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.consumer.utils.CacheUri;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.felles.Person;
import no.fint.personal.Personalressurs;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintSelfId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@FintSelfId(self = Person.class, id = "foedselsnummer.identifikatorverdi")
@FintRelation(objectLink = Personalressurs.class, id = "ansattnummer.identifikatorverdi")
@Slf4j
@RestController
@RequestMapping(value = RestEndpoints.PERSON, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonController {

    @Autowired
    private FintAuditService fintAuditService;

    @Autowired
    private PersonCacheService cacheService;


    @RequestMapping(value = "/last-updated", method = RequestMethod.GET)
    public Map<String, String> getLastUpdated(@RequestHeader(value = "x-org-id", defaultValue = "mock.no") String orgId) {
        String lastUpdated = Long.toString(cacheService.getLastUpdated(CacheUri.create(orgId, "person")));
        return ImmutableMap.of("lastUpdated", lastUpdated);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllPersoner(@RequestHeader(value = "x-org-id", defaultValue = "mock.no") String orgId,
                                         @RequestHeader(value = "x-client", defaultValue = "mock") String client,
                                         @RequestParam(required = false) Long sinceTimeStamp) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);
        log.info("SinceTimeStamp: {}", sinceTimeStamp);

        Event event = new Event(orgId, "administrasjon/personal", "GET_ALL_PERSONER", client);
        fintAuditService.audit(event, true);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event, true);

        String cacheUri = CacheUri.create(orgId, "person");
        List<Person> personer;
        if (sinceTimeStamp == null) {
            personer = cacheService.getAll(cacheUri);
        } else {
            personer = cacheService.getAll(cacheUri, sinceTimeStamp);
        }

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event, true);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event, false);

        return ResponseEntity.ok(personer);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getPerson(@PathVariable String id,
                                    @RequestHeader(value = "x-org-id", defaultValue = "mock.no") String orgId,
                                    @RequestHeader(value = "x-client", defaultValue = "mock") String client) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);

        Event event = new Event(orgId, "administrasjon/personal", "GET_PERSON", client);
        fintAuditService.audit(event, true);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event, true);

        String cacheUri = CacheUri.create(orgId, "person");
        List<Person> personer = cacheService.getAll(cacheUri);

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event, true);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event, false);

        Optional<Person> personOptional = personer.stream().filter(
                (Person person) -> person.getFoedselsnummer().getIdentifikatorverdi().equals(id)
        ).findFirst();

        if (personOptional.isPresent()) {
            return ResponseEntity.ok(personOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
