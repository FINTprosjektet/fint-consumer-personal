package no.fint.consumer;

import com.google.common.collect.ImmutableMap;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.felles.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class Config {

    private static final String contextRoot = "/administrasjons/personal";

    @Qualifier("linkMapper")
    @Autowired
    private Map<String, String> linkMapper() {
        return ImmutableMap.of(
                Personalressurs.class.getName(), personalPath("/personalressurs"),
                Arbeidsforhold.class.getName(), personalPath("/arbeidsforhold"),
                Person.class.getName(), personalPath("/person"),
                Organisasjonselement.class.getName(), "/administrasjon/organisasjon/organisasjonselement"
        );
    }

    private String personalPath(String path) {
        return String.format("%s%s", contextRoot, path);
    }

}
