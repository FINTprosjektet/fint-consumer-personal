package no.fint.consumer;

import com.google.common.collect.ImmutableMap;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.felles.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class Config {

    @Value("${server.contextPath:}")
    private String contextPath;

    @Qualifier("linkMapper")
    @Bean
    public Map<String, String> linkMapper() {
        return ImmutableMap.of(
                Personalressurs.class.getName(), fullPath("/personalressurs"),
                Arbeidsforhold.class.getName(), fullPath("/arbeidsforhold"),
                Person.class.getName(), fullPath("/person"),
                Organisasjonselement.class.getName(), "/administrasjon/organisasjon/organisasjonselement"
        );
    }

    private String fullPath(String path) {
        return String.format("%s%s", contextPath, path);
    }

}
