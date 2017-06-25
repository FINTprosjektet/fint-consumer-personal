package no.fint.consumer.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ConsumerProps {

    public static final String CACHE_INITIALDELAY_ARBEIDSFORHOLD = "${fint.consumer.cache.initialDelay.arbeidsforhold:50000}";
    public static final String CACHE_FIXEDRATE_ARBEIDSFORHOLD = "${fint.consumer.cache.fixedRate.arbeidsforhold:55000}";

    public static final String CACHE_INITIALDELAY_PERSON = "${fint.consumer.cache.initialDelay.person:40000}";
    public static final String CACHE_FIXEDRATE_PERSON = "${fint.consumer.cache.fixedRate.person:55000}";

    public static final String CACHE_INITIALDELAY_PERSONALRESSURS = "${fint.consumer.cache.initialDelay.personalressurs:30000}";
    public static final String CACHE_FIXEDRATE_PERSONALRESSURS = "${fint.consumer.cache.fixedRate.personalressurs:55000}";

    @Value("${fint.events.orgIds:mock.no}")
    private String[] orgs;



}
