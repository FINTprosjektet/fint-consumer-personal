package no.fint.consumer.personalressurs;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.consumer.event.Actions;
import no.fint.consumer.utils.CacheUri;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.model.administrasjon.personal.Personalressurs;
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

@FintSelf(type = Personalressurs.class, property = "ansattnummer")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = RestEndpoints.PERSONALRESSURS, produces = {"application/hal+json", MediaType.APPLICATION_JSON_UTF8_VALUE})
public class PersonalressursController {

    @Autowired
    private PersonalressursCacheService cacheService;

    @Autowired
    private FintAuditService fintAuditService;

    @RequestMapping(value = "/last-updated", method = RequestMethod.GET)
    public Map<String, String> getLastUpdated(@RequestHeader(value = "x-org-id") String orgId) {
        String lastUpdated = Long.toString(cacheService.getLastUpdated(CacheUri.create(orgId, "personalressurs")));
        return ImmutableMap.of("lastUpdated", lastUpdated);
    }

    @FintRelations
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getPersonalressurser(@RequestHeader(value = "x-org-id") String orgId,
                                               @RequestHeader(value = "x-client") String client,
                                               @RequestParam(required = false) Long sinceTimeStamp) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);
        log.info("SinceTimeStamp: {}", sinceTimeStamp);

        Event event = new Event(orgId, "administrasjon/personal", Actions.GET_ALL_PERSONALRESSURS.name(), client);
        fintAuditService.audit(event);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event);

        String cacheUri = CacheUri.create(orgId, "personalressurs");
        List<FintResource<Personalressurs>> personalressurser;
        if (sinceTimeStamp == null) {
            personalressurser = cacheService.getAll(cacheUri);
        } else {
            personalressurser = cacheService.getAll(cacheUri, sinceTimeStamp);
        }

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event);

        return ResponseEntity.ok(personalressurser);
    }

    @FintRelations
    @RequestMapping(value = "/ansattnummer/{id}", method = RequestMethod.GET)
    public ResponseEntity getPersonalressurs(@PathVariable String id,
                                             @RequestHeader(value = "x-org-id") String orgId,
                                             @RequestHeader(value = "x-client") String client) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);

        Event event = new Event(orgId, "administrasjon/personal", Actions.GET_PERSONALRESSURS.name(), client);
        fintAuditService.audit(event);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event);

        String cacheUri = CacheUri.create(orgId, "personalressurs");
        List<FintResource<Personalressurs>> personalressurser;

        personalressurser = cacheService.getAll(cacheUri);

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event);


        Optional<FintResource<Personalressurs>> personalressursOptional = personalressurser.stream().filter(
                personalressurs -> personalressurs.getConvertedResource().getAnsattnummer().getIdentifikatorverdi().equals(id)
        ).findFirst();

        if (personalressursOptional.isPresent()) {
            return ResponseEntity.ok(personalressursOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
