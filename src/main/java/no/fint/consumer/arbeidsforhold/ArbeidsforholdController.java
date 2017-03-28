package no.fint.consumer.arbeidsforhold;


import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.consumer.utils.CacheUri;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintSelf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FintSelf(self = Arbeidsforhold.class, id = "stillingsnummer")
@FintRelation(value = "REL_ID_PERSONALRESSURS", mainProperty = "systemId.identifikatorverdi")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = RestEndpoints.EMPLOYMENT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ArbeidsforholdController {

    @Autowired
    private FintAuditService fintAuditService;

    @Autowired
    private ArbeidsforholdCacheService cacheService;


    @RequestMapping(value = "/last-updated", method = RequestMethod.GET)
    public Map<String, String> getLastUpdated(@RequestHeader(value = "x-org-id") String orgId) {
        String lastUpdated = Long.toString(cacheService.getLastUpdated(CacheUri.create(orgId, "arbeidsforhold")));
        return ImmutableMap.of("lastUpdated", lastUpdated);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllArbeidsforhold(@RequestHeader(value = "x-org-id") String orgId,
                                               @RequestHeader(value = "x-client") String client,
                                               @RequestParam(required = false) Long sinceTimeStamp) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);
        log.info("SinceTimeStamp: {}", sinceTimeStamp);

        Event event = new Event(orgId, "administrasjon/personal", "GET_ALL_ARBEIDSFORHOLD", client);
        fintAuditService.audit(event, true);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event, true);

        String cacheUri = CacheUri.create(orgId, "arbeidsforhold");
        List<Arbeidsforhold> employments;
        if (sinceTimeStamp == null) {
            employments = cacheService.getAll(cacheUri);
        } else {
            employments = cacheService.getAll(cacheUri, sinceTimeStamp);
        }

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event, true);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event, false);

        return ResponseEntity.ok(employments);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getArbeidsforhold(@PathVariable String id,
                                            @RequestHeader(value = "x-org-id") String orgId,
                                            @RequestHeader(value = "x-client") String client) {
        log.info("Id: {}", id);
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);

        Event event = new Event(orgId, "administrasjon/personal", "GET_ARBEIDSFORHOLD", client);
        fintAuditService.audit(event, true);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event, true);

        String cacheUri = CacheUri.create(orgId, "arbeidsforhold");
        List<Arbeidsforhold> employments = cacheService.getAll(cacheUri);

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event, true);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event, false);

        Optional<Arbeidsforhold> arbeidsforholdOptional = employments.stream().filter(
                (Arbeidsforhold arbeidsforhold) -> arbeidsforhold.getSystemId().getIdentifikatorverdi().equals(id)
        ).findFirst();

        if (arbeidsforholdOptional.isPresent()) {
            return ResponseEntity.ok(arbeidsforholdOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
