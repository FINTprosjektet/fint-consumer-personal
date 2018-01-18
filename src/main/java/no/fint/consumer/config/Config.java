package no.fint.consumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.google.common.collect.ImmutableMap;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import no.fint.cache.CacheManager;
import no.fint.cache.FintCacheManager;
import no.fint.cache.HazelcastCacheManager;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.model.administrasjon.kodeverk.*;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.felles.Person;
import no.fint.model.felles.kodeverk.iso.Kjonn;
import no.fint.model.felles.kodeverk.iso.Landkode;
import no.fint.model.felles.kodeverk.iso.Sprak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;

@Configuration
public class Config {

    @Value("${server.context-path:}")
    private String contextPath;

    @Value("${fint.consumer.cache-manager:default}")
    private String cacheManagerType;

    @Value("${fint.hazelcast.members}")
    private String members;

    @Bean
    public CacheManager<?> cacheManager() {
        switch (cacheManagerType.toUpperCase()) {
            case "HAZELCAST":
                return new HazelcastCacheManager<>();
            default:
                return new FintCacheManager<>();
        }
    }

    @Bean
    public com.hazelcast.config.Config hazelcastConfig() {
        return new com.hazelcast.config.Config().setNetworkConfig(new NetworkConfig().setJoin(new JoinConfig().setTcpIpConfig(new TcpIpConfig().setMembers(Arrays.asList(members.split(","))).setEnabled(true)).setMulticastConfig(new MulticastConfig().setEnabled(false))));
    }

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper.setDateFormat(new ISO8601DateFormat());
    }

    @Qualifier("linkMapper")
    @Bean
    public Map<String, String> linkMapper() {
        return ImmutableMap.<String,String>builder()
        .put(Personalressurs.class.getName(), fullPath(RestEndpoints.PERSONALRESSURS))
        .put(Arbeidsforhold.class.getName(), fullPath(RestEndpoints.ARBEIDSFORHOLD))

        .put(Ansvar.class.getName(), "/administrasjon/kodeverk/ansvar")
        .put(Funksjon.class.getName(), "/administrasjon/kodeverk/funksjon")
        .put(Arbeidsforholdstype.class.getName(), "/administrasjon/kodeverk/arbeidsforholdstype")
        .put(Stillingskode.class.getName(), "/administrasjon/kodeverk/stillingskode")
        .put(Uketimetall.class.getName(), "/administrasjon/kodeverk/uketimetall")
        .put(Organisasjonselement.class.getName(), "/administrasjon/organisasjon/organisasjonselement")
        .put(Personalressurskategori.class.getName(), "/administrasjon/kodeverk/personalressurskategori")

        .put(Person.class.getName(), fullPath(RestEndpoints.PERSON))
        .put(Sprak.class.getName(), "/felles/kodeverk/sprak")
        .put(Landkode.class.getName(), "/felles/kodeverk/land")
        .put(Kjonn.class.getName(), "/felles/kodeverk/kjonn")
        .build();
    }

    String fullPath(String path) {
        return String.format("%s%s", contextPath, path);
    }

}
