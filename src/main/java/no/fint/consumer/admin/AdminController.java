package no.fint.consumer.admin;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheService;
import no.fint.cache.utils.CacheUri;
import no.fint.consumer.config.Constants;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.consumer.service.SubscriberService;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.DefaultActions;
import no.fint.event.model.Event;
import no.fint.event.model.HeaderConstants;
import no.fint.event.model.health.Health;
import no.fint.event.model.health.HealthStatus;
import no.fint.events.FintEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = RestEndpoints.ADMIN, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminController {

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private List<CacheService> cacheServices = Collections.emptyList();

    @Autowired
    private FintEvents fintEvents;

    @Autowired
    private SubscriberService subscriberService;

    @GetMapping("/health")
    public ResponseEntity healthCheck(@RequestHeader(value = HeaderConstants.ORG_ID, defaultValue = Constants.DEFAULT_HEADER_ORGID) String orgId,
                                      @RequestHeader(value = HeaderConstants.CLIENT, defaultValue = Constants.DEFAULT_HEADER_CLIENT) String client) {
        Event<Health> event = new Event<>(orgId, Constants.COMPONENT, DefaultActions.HEALTH.name(), client);
        event.addData(new Health(Constants.COMPONENT_CONSUMER, HealthStatus.SENT_FROM_CONSUMER_TO_PROVIDER));
        Optional<Event<Health>> health = consumerEventUtil.healthCheck(event);

        if (health.isPresent()) {
            Event<Health> receivedHealth = health.get();
            receivedHealth.addData(new Health(Constants.COMPONENT_CONSUMER, HealthStatus.RECEIVED_IN_CONSUMER_FROM_PROVIDER));
            return ResponseEntity.ok(receivedHealth);
        } else {
            event.setMessage("No response from adapter");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(event);
        }
    }

    @GetMapping("/organisations")
    public List<String> getOrganisations() {
        return CacheUri.getCacheUris(cacheServices);
    }

    @GetMapping("/organisations/{orgId:.+}")
    public List<String> getOrganization(@PathVariable String orgId) {
        List<String> cacheUris = CacheUri.getCacheUris(cacheServices);
        return cacheUris.stream().filter(key -> CacheUri.containsOrgId(key, orgId)).collect(Collectors.toList());
    }

    @PostMapping("/organisations/{orgId:.+}")
    public ResponseEntity registerOrganization(@PathVariable String orgId) {
        if (CacheUri.containsOrgId(cacheServices, orgId)) {
            return ResponseEntity.badRequest().body(String.format("OrgId %s is already registered", orgId));
        } else {
            Event event = new Event(orgId, Constants.COMPONENT, DefaultActions.REGISTER_ORG_ID.name(), "consumer");
            fintEvents.sendDownstream(event);

            cacheServices.forEach(cache -> cache.createCache(orgId));
            fintEvents.registerUpstreamListener(orgId, subscriberService);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
            return ResponseEntity.created(location).build();
        }
    }
}
