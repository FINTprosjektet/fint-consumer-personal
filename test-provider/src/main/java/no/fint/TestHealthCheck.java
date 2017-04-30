package no.fint;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.events.HealthCheck;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestHealthCheck implements HealthCheck<Event<String>> {
    @Override
    public Event<String> check(Event<String> event) {
        log.info("Event received: {}", event);
        Event<String> health = new Event<>();
        health.setCorrId(event.getCorrId());
        health.setOrgId(event.getOrgId());

        health.setAction("HEALTH");
        health.setStatus(Status.TEMP_UPSTREAM_QUEUE);
        health.setTime(System.currentTimeMillis());
        health.setSource("fk");
        health.setClient("vfs");
        event.setData(Lists.newArrayList("Reply from test-client"));
        return event;
    }
}
