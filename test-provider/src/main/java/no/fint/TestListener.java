package no.fint;

import no.fint.event.model.Event;
import no.fint.events.annotations.FintEventListener;
import org.springframework.stereotype.Component;

@Component
public class TestListener {

    @FintEventListener
    public void recieve(Event<String> event) {
        
    }
}
