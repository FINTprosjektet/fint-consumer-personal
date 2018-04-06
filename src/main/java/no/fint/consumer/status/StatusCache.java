package no.fint.consumer.status;

import no.fint.event.model.Event;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentSkipListMap;

@Component
public class StatusCache extends ConcurrentSkipListMap<String, Event> {
}
