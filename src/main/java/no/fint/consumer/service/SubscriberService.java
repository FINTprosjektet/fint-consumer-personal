package no.fint.consumer.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SubscriberService {

    public void recieve(Event event) {
        log.info("Event received: {}", event.getCorrId());
    }

}
