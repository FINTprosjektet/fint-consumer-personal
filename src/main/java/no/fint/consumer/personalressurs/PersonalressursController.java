package no.fint.consumer.personalressurs;

import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.consumer.admin.AdminService;
import no.fint.consumer.admin.Health;
import no.fint.consumer.utils.CacheUri;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.personal.Arbeidsforhold;
import no.fint.personal.Personalressurs;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintSelfId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FintSelfId(self = Personalressurs.class, id = "ansattnummer.identifikatorverdi")
@FintRelation(objectLink = Arbeidsforhold.class, id = "stillingsnummer")
@Slf4j
@RestController
@RequestMapping(value = RestEndpoints.PERSONALRESSURS, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonalressursController {


    @Autowired
    private PersonalressursCacheService cacheService;

    @Autowired
    private FintAuditService fintAuditService;

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public Health sendHealth(@RequestHeader("x-org-id") String orgId, @RequestHeader("x-client") String client) {
        return adminService.healthCheck(orgId, client);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getPersonalressurser(@RequestHeader("x-org-id") String orgId, @RequestHeader("x-client") String client, @RequestParam(required = false) Long sinceTimeStamp) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);
        log.info("SinceTimeStamp: {}", sinceTimeStamp);

        Event event = new Event(orgId, "employee", "GET_ALL_EMPLOYEES", client);
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

}
