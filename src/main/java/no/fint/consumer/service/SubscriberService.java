package no.fint.consumer.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheService;
import no.fint.event.model.Event;
import no.fint.events.annotations.FintEventListener;
import no.fint.events.queue.QueueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SubscriberService {

    @Autowired
    private List<CacheService> cacheServices;

    @FintEventListener(type = QueueType.UPSTREAM)
    public void receive(Event event) {
        log.debug("Received event: {}", event);
        String action = event.getAction();
        List<CacheService> supportedCacheServices = cacheServices.stream().filter(cacheService -> cacheService.supportsAction(action)).collect(Collectors.toList());
        if (supportedCacheServices.size() > 0) {
            supportedCacheServices.forEach(cacheService -> cacheService.onAction(event));
        } else {
            log.warn("Unhandled event: {}", event.getAction());
        }
    }
}
