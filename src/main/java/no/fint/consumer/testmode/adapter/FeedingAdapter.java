package no.fint.consumer.testmode.adapter;

import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.config.Constants;
import no.fint.consumer.testmode.EnabledIfTestMode;
import no.fint.event.model.DefaultActions;
import no.fint.event.model.Event;
import no.fint.event.model.HeaderConstants;
import no.fint.event.model.Status;
import no.fint.events.FintEvents;
import no.fint.model.administrasjon.personal.PersonalActions;
import no.fint.model.felles.FellesActions;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResources;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.PersonResources;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@EnabledIfTestMode
@Slf4j
@Service
public class FeedingAdapter {

    private final FintEvents fintEvents;
    private final RestTemplate restTemplate;

    public FeedingAdapter(RestTemplateBuilder builder,
                          FintEvents fintEvents,
                          @Value("${fint.consumer.test.feed-uri}") String feedUri) {
        restTemplate = builder.rootUri(feedUri).build();
        this.fintEvents = fintEvents;
    }

    @Scheduled(initialDelay = 10000, fixedDelayString = Constants.CACHE_FIXEDRATE_ARBEIDSFORHOLD)
    public void updateFromFeed() {
        log.info("Updating from feed...");
        long start = System.nanoTime();
        String[] assets = restTemplate.getForObject("/admin/assets", String[].class);
        for (String asset : assets) {
            log.info("Updating {}...", asset);
            fintEvents.registerDownstreamListener(asset, ev -> log.info("{}: {}", ev.getOrgId(), ev.getAction()));
            fintEvents.sendUpstream(new Event(asset, "feed", DefaultActions.REGISTER_ORG_ID, "feed"));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HeaderConstants.ORG_ID, asset);
            headers.add(HeaderConstants.CLIENT, "feeder");

            try {
                ResponseEntity<ArbeidsforholdResources> arbeidsforhold = restTemplate.exchange("/arbeidsforhold", HttpMethod.GET, new HttpEntity<>(headers), ArbeidsforholdResources.class);
                Event<ArbeidsforholdResource> event = new Event<>(asset, "arbeidsforhold", PersonalActions.GET_ALL_ARBEIDSFORHOLD, "feed");
                event.setData(arbeidsforhold.getBody().getContent());
                event.setStatus(Status.ADAPTER_RESPONSE);
                fintEvents.sendUpstream(event);
            } catch (Exception e) {
                log.warn("Arbeidsforhold", e);
            }

            try {
                ResponseEntity<PersonalressursResources> personalressurs = restTemplate.exchange("/personalressurs", HttpMethod.GET, new HttpEntity<>(headers), PersonalressursResources.class);
                Event<PersonalressursResource> event = new Event<>(asset, "personalressurs", PersonalActions.GET_ALL_PERSONALRESSURS, "feed");
                event.setData(personalressurs.getBody().getContent());
                event.setStatus(Status.ADAPTER_RESPONSE);
                fintEvents.sendUpstream(event);
            } catch (Exception e) {
                log.warn("Personalressurs", e);
            }

            try {
                ResponseEntity<PersonResources> person = restTemplate.exchange("/person", HttpMethod.GET, new HttpEntity<>(headers), PersonResources.class);
                Event<PersonResource> event = new Event<>(asset, "person", FellesActions.GET_ALL_PERSON, "feed");
                event.setData(person.getBody().getContent());
                event.setStatus(Status.ADAPTER_RESPONSE);
                fintEvents.sendUpstream(event);
            } catch (Exception e) {
                log.warn("Person", e);
            }
        }
        long duration = System.nanoTime() - start;
        log.info("Update completed in {}", Duration.ofNanos(duration));
    }
}
