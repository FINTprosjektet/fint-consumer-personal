package no.fint.consumer.models.fastlonn;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.Event;
import no.fint.event.model.HeaderConstants;
import no.fint.event.model.Status;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.relation.FintResource;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import no.fint.relations.FintRelationsMediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import no.fint.model.administrasjon.personal.Fastlonn;
import no.fint.model.administrasjon.personal.PersonalActions;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = RestEndpoints.FASTLONN, produces = {FintRelationsMediaType.APPLICATION_HAL_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
public class FastlonnController {

    @Autowired
    private FastlonnCacheService cacheService;

    @Autowired
    private FintAuditService fintAuditService;

    @Autowired
    private FastlonnAssembler assembler;

    @Autowired
    private ConsumerProps props;

    @GetMapping("/last-updated")
    public Map<String, String> getLastUpdated(@RequestHeader(name = HeaderConstants.ORG_ID, required = false) String orgId) {
        if (props.isOverrideOrgId() || orgId == null) {
            orgId = props.getDefaultOrgId();
        }
        String lastUpdated = Long.toString(cacheService.getLastUpdated(orgId));
        return ImmutableMap.of("lastUpdated", lastUpdated);
    }

    @GetMapping("/cache/size")
    public ImmutableMap<String, Integer> getCacheSize(@RequestHeader(name = HeaderConstants.ORG_ID, required = false) String orgId) {
        if (props.isOverrideOrgId() || orgId == null) {
            orgId = props.getDefaultOrgId();
        }
        return ImmutableMap.of("size", cacheService.getAll(orgId).size());
    }

    @PostMapping("/cache/rebuild")
    public void rebuildCache(@RequestHeader(name = HeaderConstants.ORG_ID, required = false) String orgId) {
        if (props.isOverrideOrgId() || orgId == null) {
            orgId = props.getDefaultOrgId();
        }
        cacheService.rebuildCache(orgId);
    }

    @GetMapping
    public ResponseEntity getFastlonn(
            @RequestHeader(name = HeaderConstants.ORG_ID, required = false) String orgId,
            @RequestHeader(name = HeaderConstants.CLIENT, required = false) String client,
            @RequestParam(required = false) Long sinceTimeStamp) {
        if (props.isOverrideOrgId() || orgId == null) {
            orgId = props.getDefaultOrgId();
        }
        if (client == null) {
            client = props.getDefaultClient();
        }
        log.info("OrgId: {}, Client: {}", orgId, client);

        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_FASTLONN, client);
        fintAuditService.audit(event);

        fintAuditService.audit(event, Status.CACHE);

        List<FastlonnResource> fastlonn;
        if (sinceTimeStamp == null) {
            fastlonn = cacheService.getAll(orgId);
        } else {
            fastlonn = cacheService.getAll(orgId, sinceTimeStamp);
        }

        fintAuditService.audit(event, Status.CACHE_RESPONSE, Status.SENT_TO_CLIENT);

        return ResponseEntity.ok(new Resources(fastlonn, new org.springframework.hateoas.Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toString())));
    }


    @GetMapping("/systemid/{id}")
    public ResponseEntity getFastlonnBySystemId(@PathVariable String id,
                                                @RequestHeader(name = HeaderConstants.ORG_ID, required = false) String orgId,
                                                @RequestHeader(name = HeaderConstants.CLIENT, required = false) String client) {
        if (props.isOverrideOrgId() || orgId == null) {
            orgId = props.getDefaultOrgId();
        }
        if (client == null) {
            client = props.getDefaultClient();
        }
        log.info("SystemId: {}, OrgId: {}, Client: {}", id, orgId, client);

        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_FASTLONN, client);
        fintAuditService.audit(event);

        fintAuditService.audit(event, Status.CACHE);

        Optional<FastlonnResource> fastlonn = cacheService.getFastlonnBySystemId(orgId, id);

        fintAuditService.audit(event, Status.CACHE_RESPONSE, Status.SENT_TO_CLIENT);

        if (fastlonn.isPresent()) {
            return ResponseEntity.ok(fastlonn.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity createFastlonn(
            @RequestHeader(name = HeaderConstants.ORG_ID, required = false) String orgId,
            @RequestHeader(name = HeaderConstants.CLIENT, required = false) String client,
            @RequestBody FastlonnResource body) {
        log.info("Body: {}", body);
        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi(Long.toString(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)));
        body.setSystemId(systemId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("systemid", "{id}").buildAndExpand(systemId.getIdentifikatorverdi()).toUri();
        log.info("URI: {}", uri);
        body.addLink("self", Link.with(uri.toString()));
        cacheService.add(orgId, Collections.singletonList(body));
        return ResponseEntity.created(uri).build();
    }
}

