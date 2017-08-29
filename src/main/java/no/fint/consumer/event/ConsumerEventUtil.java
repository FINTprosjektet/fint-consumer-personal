package no.fint.consumer.event;

import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.event.model.health.Health;
import no.fint.events.FintEvents;
import no.fint.events.FintEventsHealth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ConsumerEventUtil {

    @Autowired
    private FintEvents fintEvents;

    @Autowired
    private FintEventsHealth fintEventsHealth;

    @Autowired
    private FintAuditService fintAuditService;

    public Optional<Event<Health>> healthCheck(Event event) {
        fintAuditService.audit(event);

        fintAuditService.audit(event, Status.DOWNSTREAM_QUEUE);

        log.info("Sending health check to {}", event.getOrgId());
        Event response = fintEventsHealth.sendHealthCheck(event.getOrgId(), event.getCorrId(), event);
        if (response == null) {
            return Optional.empty();
        } else {
            fintAuditService.audit(response, Status.SENT_TO_CLIENT);
            return Optional.of(response);
        }
    }

    public void send(Event event) {
        fintAuditService.audit(event);

        fintAuditService.audit(event, Status.DOWNSTREAM_QUEUE);

        log.info("Sending event {} to {}", event.getAction(), event.getOrgId());
        fintEvents.sendDownstream(event.getOrgId(), event);
        fintAuditService.audit(event, Status.SENT_TO_CLIENT);
    }

}
