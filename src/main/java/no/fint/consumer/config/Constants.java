
package no.fint.consumer.config;

public enum Constants {
;

    public static final String COMPONENT = "personal";
    public static final String COMPONENT_CONSUMER = COMPONENT + " consumer";
    public static final String CACHE_SERVICE = "CACHE_SERVICE";

    
    public static final String CACHE_INITIALDELAY_ARBEIDSFORHOLD = "${fint.consumer.cache.initialDelay.arbeidsforhold:60000}";
    public static final String CACHE_FIXEDRATE_ARBEIDSFORHOLD = "${fint.consumer.cache.fixedRate.arbeidsforhold:900000}";
    
    public static final String CACHE_INITIALDELAY_FASTLONN = "${fint.consumer.cache.initialDelay.fastlonn:70000}";
    public static final String CACHE_FIXEDRATE_FASTLONN = "${fint.consumer.cache.fixedRate.fastlonn:900000}";
    
    public static final String CACHE_INITIALDELAY_FASTTILLEGG = "${fint.consumer.cache.initialDelay.fasttillegg:80000}";
    public static final String CACHE_FIXEDRATE_FASTTILLEGG = "${fint.consumer.cache.fixedRate.fasttillegg:900000}";
    
    public static final String CACHE_INITIALDELAY_FRAVAR = "${fint.consumer.cache.initialDelay.fravar:90000}";
    public static final String CACHE_FIXEDRATE_FRAVAR = "${fint.consumer.cache.fixedRate.fravar:900000}";
    
    public static final String CACHE_INITIALDELAY_KONTAKTPERSON = "${fint.consumer.cache.initialDelay.kontaktperson:100000}";
    public static final String CACHE_FIXEDRATE_KONTAKTPERSON = "${fint.consumer.cache.fixedRate.kontaktperson:900000}";
    
    public static final String CACHE_INITIALDELAY_PERSON = "${fint.consumer.cache.initialDelay.person:110000}";
    public static final String CACHE_FIXEDRATE_PERSON = "${fint.consumer.cache.fixedRate.person:900000}";
    
    public static final String CACHE_INITIALDELAY_PERSONALRESSURS = "${fint.consumer.cache.initialDelay.personalressurs:120000}";
    public static final String CACHE_FIXEDRATE_PERSONALRESSURS = "${fint.consumer.cache.fixedRate.personalressurs:900000}";
    
    public static final String CACHE_INITIALDELAY_VARIABELLONN = "${fint.consumer.cache.initialDelay.variabellonn:130000}";
    public static final String CACHE_FIXEDRATE_VARIABELLONN = "${fint.consumer.cache.fixedRate.variabellonn:900000}";
    

}
