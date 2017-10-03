package no.fint.consumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.model.administrasjon.kodeverk.*;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.felles.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {

    @Value("${server.context-path:}")
    private String contextPath;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper.setDateFormat(new ISO8601DateFormat());
    }

    @Qualifier("linkMapper")
    @Bean
    public Map<String, String> linkMapper() {
        Map<String, String> links = new HashMap<>();
        links.put(Personalressurs.class.getName(), fullPath(RestEndpoints.PERSONALRESSURS));
        links.put(Arbeidsforhold.class.getName(), fullPath(RestEndpoints.ARBEIDSFORHOLD));
        links.put(Person.class.getName(), fullPath(RestEndpoints.PERSON));
        links.put(Ansvar.class.getName(), "/administrasjon/kodeverk/ansvar/systemid");
        links.put(Funksjon.class.getName(), "/administrasjon/kodeverk/funksjon/systemid");
        links.put(Arbeidsforholdstype.class.getName(), "/administrasjon/kodeverk/arbeidsforholdstype/systemid");
        links.put(Stillingskode.class.getName(), "/administrasjon/kodeverk/stillingskode/systemid");
        links.put(Uketimetall.class.getName(), "/administrasjon/kodeverk/timerperuke/systemid");
        links.put(Organisasjonselement.class.getName(), "/administrasjon/organisasjon/organisasjonselement");
        return links;
    }

    String fullPath(String path) {
        return String.format("%s%s", contextPath, path);
    }

}
