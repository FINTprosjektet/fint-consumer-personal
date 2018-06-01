package no.fint.consumer.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ConsumerProps {
    
    @Value("${fint.consumer.override-org-id:false}")
    private boolean overrideOrgId;

    @Value("${fint.consumer.default-client:FINT}")
    private String defaultClient;

    @Value("${fint.consumer.default-org-id:fint.no}")
    private String defaultOrgId;
    
    @Value("${fint.events.orgIds:fint.no}")
    private String[] orgs;

    
    public static final String CACHE_INITIALDELAY_ARBEIDSFORHOLD = "${fint.consumer.cache.initialDelay.arbeidsforhold:60000}";
    public static final String CACHE_FIXEDRATE_ARBEIDSFORHOLD = "${fint.consumer.cache.fixedRate.arbeidsforhold:900000}";
    
    public static final String CACHE_INITIALDELAY_FASTLONN = "${fint.consumer.cache.initialDelay.fastlonn:70000}";
    public static final String CACHE_FIXEDRATE_FASTLONN = "${fint.consumer.cache.fixedRate.fastlonn:900000}";
    
    public static final String CACHE_INITIALDELAY_FRAVAR = "${fint.consumer.cache.initialDelay.fravar:80000}";
    public static final String CACHE_FIXEDRATE_FRAVAR = "${fint.consumer.cache.fixedRate.fravar:900000}";
    
    public static final String CACHE_INITIALDELAY_KONTAKTPERSON = "${fint.consumer.cache.initialDelay.kontaktperson:90000}";
    public static final String CACHE_FIXEDRATE_KONTAKTPERSON = "${fint.consumer.cache.fixedRate.kontaktperson:900000}";
    
    public static final String CACHE_INITIALDELAY_PERSON = "${fint.consumer.cache.initialDelay.person:100000}";
    public static final String CACHE_FIXEDRATE_PERSON = "${fint.consumer.cache.fixedRate.person:900000}";
    
    public static final String CACHE_INITIALDELAY_PERSONALRESSURS = "${fint.consumer.cache.initialDelay.personalressurs:110000}";
    public static final String CACHE_FIXEDRATE_PERSONALRESSURS = "${fint.consumer.cache.fixedRate.personalressurs:900000}";
    
    public static final String CACHE_INITIALDELAY_VARIABELLONN = "${fint.consumer.cache.initialDelay.variabellonn:120000}";
    public static final String CACHE_FIXEDRATE_VARIABELLONN = "${fint.consumer.cache.fixedRate.variabellonn:900000}";
    

}
