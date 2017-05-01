package no.fint;

import no.fint.events.FintEvents;
import no.fint.events.FintEventsHealth;
import no.fint.events.annotations.EnableFintEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@EnableFintEvents
@SpringBootApplication
public class Application {

    @Autowired
    private FintEvents fintEvents;

    @Autowired
    private FintEventsHealth fintEventsHealth;

    @PostConstruct
    public void init() {
        fintEvents.registerDownstreamListener(TestListener.class, "mock.no");
        fintEventsHealth.registerServer(TestHealthCheck.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
