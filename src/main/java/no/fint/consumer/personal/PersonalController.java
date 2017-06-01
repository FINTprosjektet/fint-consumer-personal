package no.fint.consumer.personal;

import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.event.model.DefaultActions;
import no.fint.event.model.Event;
import no.fint.event.model.health.Health;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = RestEndpoints.PERSONAL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonalController {

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public Event<Health> sendHealth(@RequestHeader(value = "x-org-id") String orgId,
                                    @RequestHeader(value = "x-client") String client) {
        Event<Health> event = new Event<>(orgId, "administrasjon/personal", DefaultActions.HEALTH.name(), client);
        Optional<Event> health = consumerEventUtil.healthCheck(event);
        if (health.isPresent()) {
            return new Event<>(health.get());
        } else {
            event.setMessage("No response from adapter");
            return event;
        }
    }
}
