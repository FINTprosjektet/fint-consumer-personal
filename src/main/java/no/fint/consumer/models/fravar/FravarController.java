package no.fint.consumer.models.fravar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.consumer.status.StatusCache;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.Event;
import no.fint.event.model.HeaderConstants;
import no.fint.event.model.Status;
import no.fint.model.administrasjon.personal.Fravar;
import no.fint.model.administrasjon.personal.PersonalActions;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintRelationsMediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = RestEndpoints.FRAVAR, produces = {FintRelationsMediaType.APPLICATION_HAL_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
public class FravarController {

    @Autowired
    private FravarCacheService cacheService;

    @Autowired
    private FintAuditService fintAuditService;

    @Autowired
    private FravarAssembler assembler;

    @Autowired
    private ConsumerProps props;

    @Autowired
    private StatusCache statusCache;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @GetMapping("/status/{id}")
    public ResponseEntity getStatus(@PathVariable String id,
                                    @RequestHeader(HeaderConstants.ORG_ID) String orgId,
                                    @RequestHeader(HeaderConstants.CLIENT) String client) {
        log.info("/status/{} for {} from {}", id, orgId, client);
        if (!statusCache.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        Event e = statusCache.get(id);
        log.info("Event: {}", e);
        log.info("Data: {}", e.getData());
        if (!e.getOrgId().equals(orgId)) {
            return ResponseEntity.badRequest().build();
        }
        if (e.getResponseStatus() == null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        List<FintResource<Fravar>> result = objectMapper.convertValue(e.getData(), new TypeReference<List<FintResource<Fravar>>>() {});
        switch (e.getResponseStatus()) {
            case ACCEPTED:
                return assembler.resources(result);
            case ERROR:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            case CONFLICT:
                return ResponseEntity.status(HttpStatus.CONFLICT).body(assembler.toResources(result));
            case REJECTED:
                return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

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
    public ResponseEntity getFravar(
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

        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_FRAVAR, client);
        fintAuditService.audit(event);

        fintAuditService.audit(event, Status.CACHE);

        List<FintResource<Fravar>> fravar;
        if (sinceTimeStamp == null) {
            fravar = cacheService.getAll(orgId);
        } else {
            fravar = cacheService.getAll(orgId, sinceTimeStamp);
        }

        fintAuditService.audit(event, Status.CACHE_RESPONSE, Status.SENT_TO_CLIENT);

        return assembler.resources(fravar);
    }

    @PostMapping
    public ResponseEntity postFravar(
            @RequestHeader(name = HeaderConstants.ORG_ID, required = false) String orgId,
            @RequestHeader(name = HeaderConstants.CLIENT, required = false) String client,
            @RequestBody FintResource<Fravar> body
    ) throws JsonProcessingException {
        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.UPDATE_FRAVAR, client);
        // FIXME: Explicit marshalling to JSON
        event.addObject(new ObjectMapper().disableDefaultTyping().writeValueAsString(body));
        //event.addObject(FintResource.with(data));
        fintAuditService.audit(event);

        consumerEventUtil.send(event);

        statusCache.put(event.getCorrId(), event);

        // TODO Use fint-relations and linkMapper to produce link!
        URI location = ServletUriComponentsBuilder.fromCurrentServletMapping().scheme(null).pathSegment(RestEndpoints.FRAVAR, "status", event.getCorrId()).build().toUri();
        log.info("Location: {}", location);
        return ResponseEntity.status(HttpStatus.ACCEPTED).location(location).build();
    }


    @GetMapping("/systemid/{id}")
    public ResponseEntity getFravarBySystemId(@PathVariable String id,
                                              @RequestHeader(name = HeaderConstants.ORG_ID, required = false) String orgId,
                                              @RequestHeader(name = HeaderConstants.CLIENT, required = false) String client) {
        if (props.isOverrideOrgId() || orgId == null) {
            orgId = props.getDefaultOrgId();
        }
        if (client == null) {
            client = props.getDefaultClient();
        }
        log.info("SystemId: {}, OrgId: {}, Client: {}", id, orgId, client);

        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_FRAVAR, client);
        fintAuditService.audit(event);

        fintAuditService.audit(event, Status.CACHE);

        Optional<FintResource<Fravar>> fravar = cacheService.getFravarBySystemId(orgId, id);

        fintAuditService.audit(event, Status.CACHE_RESPONSE, Status.SENT_TO_CLIENT);

        if (fravar.isPresent()) {
            return assembler.resource(fravar.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

