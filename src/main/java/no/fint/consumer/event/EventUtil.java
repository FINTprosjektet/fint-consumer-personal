package no.fint.consumer.event;

import lombok.extern.slf4j.Slf4j;
import no.fint.audit.FintAuditService;
import no.fint.consumer.service.SubscriberService;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.events.FintEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Service
public class EventUtil {

    @Autowired
    private FintEvents fintEvents;

    @Autowired
    private FintAuditService fintAuditService;

    @PostConstruct
    public void init() {
        fintEvents.registerUpstreamListener(SubscriberService.class);
    }

    public Optional<Event> sendAndReceive(Event event) {
        fintAuditService.audit(event, true);

        event.setStatus(Status.DOWNSTREAM_QUEUE);
        fintAuditService.audit(event, true);

        log.info("Sending replyTo event {} to {}", event.getAction(), event.getOrgId());
        Optional<Event> response = fintEvents.sendAndReceiveDownstream(event.getOrgId(), event, Event.class);
        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event, true);

        return response;
    }

    public void send(Event event) {
        fintAuditService.audit(event, true);

        event.setStatus(Status.DOWNSTREAM_QUEUE);
        fintAuditService.audit(event, true);

        log.info("Sending replyTo event {} to {}", event.getAction(), event.getOrgId());
        fintEvents.sendDownstream(event.getOrgId(), event);
        event.setStatus(Status.SENT_TO_CLIENT);
        fintAuditService.audit(event, true);
    }

}
