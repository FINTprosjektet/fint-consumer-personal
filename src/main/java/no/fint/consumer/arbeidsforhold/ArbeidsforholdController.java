package no.fint.consumer.arbeidsforhold;


import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.consumer.config.Constants;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.Event;
import no.fint.event.model.HeaderConstants;
import no.fint.event.model.Status;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.PersonalActions;
import no.fint.model.relation.FintResource;
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
@RequestMapping(value = RestEndpoints.ARBEIDSFORHOLD, produces = {"application/hal+json", MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ArbeidsforholdController {

    @Autowired
    private FintAuditService fintAuditService;

    @Autowired
    private ArbeidsforholdCacheService cacheService;

    @Autowired
    private ArbeidsforholdAssembler assembler;

    @RequestMapping(value = "/last-updated", method = RequestMethod.GET)
    public Map<String, String> getLastUpdated(@RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId) {
        String lastUpdated = Long.toString(cacheService.getLastUpdated(orgId));
        return ImmutableMap.of("lastUpdated", lastUpdated);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllArbeidsforhold(@RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId,
                                               @RequestHeader(value = HeaderConstants.CLIENT, defaultValue = Constants.DEFAULT_HEADER_CLIENT) String client,
                                               @RequestParam(required = false) Long sinceTimeStamp) {
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);
        log.info("SinceTimeStamp: {}", sinceTimeStamp);

        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_ARBEIDSFORHOLD, client);
        fintAuditService.audit(event);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event);

        List<FintResource<Arbeidsforhold>> employments;
        if (sinceTimeStamp == null) {
            employments = cacheService.getAll(orgId);
        } else {
            employments = cacheService.getAll(orgId, sinceTimeStamp);
        }

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event);

        return assembler.resources(employments);
    }

    @RequestMapping(value = {"/systemId/{id:.+}", "/systemid/{id:.+}"}, method = RequestMethod.GET)
    public ResponseEntity getArbeidsforhold(@PathVariable String id,
                                            @RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId,
                                            @RequestHeader(value = HeaderConstants.CLIENT, defaultValue = Constants.DEFAULT_HEADER_CLIENT) String client) {
        log.info("Id: {}", id);
        log.info("OrgId: {}", orgId);
        log.info("Client: {}", client);

        Event event = new Event(orgId, "administrasjon/personal", PersonalActions.GET_ARBEIDSFORHOLD, client);
        fintAuditService.audit(event);

        event.setStatus(Status.CACHE);
        fintAuditService.audit(event);

        List<FintResource<Arbeidsforhold>> employments = cacheService.getAll(orgId);

        event.setStatus(Status.CACHE_RESPONSE);
        fintAuditService.audit(event);

        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event);

        Optional<FintResource<Arbeidsforhold>> arbeidsforholdOptional = employments.stream().filter(
                arbeidsforhold -> arbeidsforhold.getResource().getSystemId().getIdentifikatorverdi().equals(id)
        ).findFirst();

        if (arbeidsforholdOptional.isPresent()) {
            return assembler.resource(arbeidsforholdOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
