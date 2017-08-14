package no.fint.consumer.config;

import no.fint.consumer.utils.RestEndpoints;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.felles.Person;
import no.fint.relations.FintLinkMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {

    @Value("${server.context-path:}")
    private String contextPath;

    @Qualifier("linkMapper")
    @Bean
    public Map<String, String> linkMapper() {
        Map<String, String> links = new HashMap<>();
        links.put(Personalressurs.class.getName(), fullPath(RestEndpoints.PERSONALRESSURS));
        links.put(FintLinkMapper.getName(Personalressurs.class), fullPath(RestEndpoints.PERSONALRESSURS));
        links.put(Arbeidsforhold.class.getName(), fullPath(RestEndpoints.ARBEIDSFORHOLD));
        links.put(FintLinkMapper.getName(Arbeidsforhold.class), fullPath(RestEndpoints.ARBEIDSFORHOLD));
        links.put(Person.class.getName(), fullPath(RestEndpoints.PERSON));
        links.put(FintLinkMapper.getName(Person.class), fullPath(RestEndpoints.PERSON));
        links.put(Organisasjonselement.class.getName(), "/administrasjon/organisasjon/organisasjonselement");
        links.put(FintLinkMapper.getName(Organisasjonselement.class), "/administrasjon/organisasjon/organisasjonselement");
        return links;
    }

    String fullPath(String path) {
        return String.format("%s%s", contextPath, path);
    }

}
