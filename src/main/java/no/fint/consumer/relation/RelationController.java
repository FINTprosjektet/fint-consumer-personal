package no.fint.consumer.relation;

import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.consumer.utils.CacheUri;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.model.relation.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = RestEndpoints.RELATION, produces = {"application/hal+json", "application/ld+json", MediaType.APPLICATION_JSON_UTF8_VALUE})
public class RelationController {

    @Autowired
    private RelationCacheService cacheService;

    @Autowired
    private FintAuditService fintAuditService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllRelations(@RequestHeader("x-org-id") String orgId, @RequestHeader("x-client") String client, @RequestParam(required = false) Long sinceTimeStamp) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);
        log.info("SinceTimeStamp: {}", sinceTimeStamp);

        Event event = new Event(orgId, "administrasjon/personal", "GET_ALL_RELATIONS", client);
        fintAuditService.audit(event, true);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event, true);

        String cacheUri = CacheUri.create(orgId, "relation");
        List<Relation> relations;
        if (sinceTimeStamp == null) {
            relations = cacheService.getAll(cacheUri);
        } else {
            relations = cacheService.getAll(cacheUri, sinceTimeStamp);
        }

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event, true);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event, false);

        return ResponseEntity.ok(relations);
    }
}
