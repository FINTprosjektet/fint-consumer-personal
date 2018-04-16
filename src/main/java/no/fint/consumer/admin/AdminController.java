package no.fint.consumer.admin;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheManager;
import no.fint.cache.CacheService;
import no.fint.cache.utils.CacheUri;
import no.fint.consumer.config.Constants;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.consumer.event.EventListener;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = RestEndpoints.ADMIN, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminController {

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private CacheManager<?> cacheManager;

    @Autowired
    private FintEvents fintEvents;

    @Autowired
    private EventListener eventListener;

    @GetMapping("/health")
    public ResponseEntity healthCheck(@RequestHeader(HeaderConstants.ORG_ID) String orgId,
                                      @RequestHeader(HeaderConstants.CLIENT) String client) {
        Event<Health> event = new Event<>(orgId, Constants.COMPONENT, DefaultActions.HEALTH, client);
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
    public Collection<String> getOrganisations() {
        return cacheManager.getKeys();
    }

    @GetMapping("/organisations/{orgId:.+}")
    public Collection<String> getOrganization(@PathVariable String orgId) {
        return cacheManager.getKeys().stream().filter(key -> CacheUri.containsOrgId(key, orgId)).collect(Collectors.toList());
    }

    /*
    @PostMapping("/organisations/{orgId:.+}")
    public ResponseEntity registerOrganization(@PathVariable String orgId) {
        if (CacheUri.containsOrgId(cacheServices, orgId)) {
            return ResponseEntity.badRequest().body(String.format("OrgId %s is already registered", orgId));
        } else {
            Event event = new Event(orgId, Constants.COMPONENT, DefaultActions.REGISTER_ORG_ID, "consumer");
            fintEvents.sendDownstream(event);

            cacheServices.forEach(cache -> cache.createCache(orgId));
            fintEvents.registerUpstreamListener(orgId, eventListener);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri();
            return ResponseEntity.created(location).build();
        }
    }
*/
}