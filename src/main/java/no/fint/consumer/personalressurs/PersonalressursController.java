package no.fint.consumer.personalressurs;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.consumer.utils.CacheUri;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.felles.Person;
import no.fint.personal.Arbeidsforhold;
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

@FintSelfId(self = Personalressurs.class, id = "ansattnummer.identifikatorverdi")
@FintRelation(objectLink = Arbeidsforhold.class, id = "stillingsnummer")
@FintRelation(objectLink = Person.class, id = "foedselsnummer.identifikatorverdi")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = RestEndpoints.PERSONALRESSURS, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonalressursController {

    @Autowired
    private PersonalressursCacheService cacheService;

    @Autowired
    private FintAuditService fintAuditService;


    @RequestMapping(value = "/last-updated", method = RequestMethod.GET)
    public Map<String, String> getLastUpdated(@RequestHeader(value = "x-org-id", defaultValue = "mock.no") String orgId) {
        String lastUpdated = Long.toString(cacheService.getLastUpdated(CacheUri.create(orgId, "personalressurs")));
        return ImmutableMap.of("lastUpdated", lastUpdated);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getPersonalressurser(@RequestHeader(value = "x-org-id", defaultValue = "mock.no") String orgId,
                                               @RequestHeader(value = "x-client", defaultValue = "mock") String client,
                                               @RequestParam(required = false) Long sinceTimeStamp) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);
        log.info("SinceTimeStamp: {}", sinceTimeStamp);

        Event event = new Event(orgId, "administrasjon/personal", "GET_ALL_EMPLOYEES", client);
        fintAuditService.audit(event, true);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event, true);

        String cacheUri = CacheUri.create(orgId, "personalressurs");
        List<Personalressurs> personalressurser;
        if (sinceTimeStamp == null) {
            personalressurser = cacheService.getAll(cacheUri);
        } else {
            personalressurser = cacheService.getAll(cacheUri, sinceTimeStamp);
        }

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event, true);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event, false);

        return ResponseEntity.ok(personalressurser);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getPersonalressurs(@PathVariable String id,
                                             @RequestHeader(value = "x-org-id", defaultValue = "mock.no") String orgId,
                                             @RequestHeader(value = "x-client", defaultValue = "mock") String client) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);

        Event event = new Event(orgId, "administrasjon/personal", "GET_EMPLOYEE", client);
        fintAuditService.audit(event, true);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event, true);

        String cacheUri = CacheUri.create(orgId, "personalressurs");
        List<Personalressurs> personalressurser;

        personalressurser = cacheService.getAll(cacheUri);

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event, true);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event, false);


        Optional<Personalressurs> personalressursOptional = personalressurser.stream().filter(
                (Personalressurs personalressurs) -> personalressurs.getAnsattnummer().getIdentifikatorverdi().equals(id)
        ).findFirst();

        if (personalressursOptional.isPresent()) {
            return ResponseEntity.ok(personalressursOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
