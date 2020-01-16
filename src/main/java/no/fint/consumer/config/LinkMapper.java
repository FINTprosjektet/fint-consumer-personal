package no.fint.consumer.config;

import com.google.common.collect.ImmutableMap;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.model.administrasjon.fullmakt.Fullmakt;
import no.fint.model.administrasjon.fullmakt.Rolle;
import no.fint.model.administrasjon.kodeverk.*;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.administrasjon.personal.*;
import no.fint.model.felles.Kontaktperson;
import no.fint.model.felles.Person;
import no.fint.model.felles.kodeverk.iso.Kjonn;
import no.fint.model.felles.kodeverk.iso.Landkode;
import no.fint.model.felles.kodeverk.iso.Sprak;

import java.util.Map;

public class LinkMapper {

    public static Map<String, String> linkMapper(String contextPath) {
        return ImmutableMap.<String, String>builder()
                .put(Arbeidsforhold.class.getName(), contextPath + RestEndpoints.ARBEIDSFORHOLD)
                .put(Fastlonn.class.getName(), contextPath + RestEndpoints.FASTLONN)
                .put(Fasttillegg.class.getName(), contextPath + RestEndpoints.FASTTILLEGG)
                .put(Fravar.class.getName(), contextPath + RestEndpoints.FRAVAR)
                .put(Person.class.getName(), contextPath + RestEndpoints.PERSON)
                .put(Kontaktperson.class.getName(), contextPath + RestEndpoints.KONTAKTPERSON)
                .put(Personalressurs.class.getName(), contextPath + RestEndpoints.PERSONALRESSURS)
                .put(Variabellonn.class.getName(), contextPath + RestEndpoints.VARIABELLONN)

                .put(Aktivitet.class.getName(), "/administrasjon/kodeverk/aktivitet")
                .put(Anlegg.class.getName(), "/administrasjon/kodeverk/anlegg")
                .put(Ansvar.class.getName(), "/administrasjon/kodeverk/ansvar")
                .put(Arbeidsforholdstype.class.getName(), "/administrasjon/kodeverk/arbeidsforholdstype")
                .put(Art.class.getName(), "/administrasjon/kodeverk/art")
                .put(Diverse.class.getName(), "/administrasjon/kodeverk/diverse")
                .put(Fravarsgrunn.class.getName(), "/administrasjon/kodeverk/fravarsgrunn")
                .put(Fravarstype.class.getName(), "/administrasjon/kodeverk/fravarstype")
                .put(Funksjon.class.getName(), "/administrasjon/kodeverk/funksjon")
                .put(Kontrakt.class.getName(), "/administrasjon/kodeverk/kontrakt")
                .put(Lonnsart.class.getName(), "/administrasjon/kodeverk/lonnsart")
                .put(Lopenummer.class.getName(), "/administrasjon/kodeverk/lopenummer")
                .put(Objekt.class.getName(), "/administrasjon/kodeverk/objekt")
                .put(Personalressurskategori.class.getName(), "/administrasjon/kodeverk/personalressurskategori")
                .put(Prosjekt.class.getName(), "/administrasjon/kodeverk/prosjekt")
                .put(Ramme.class.getName(), "/administrasjon/kodeverk/ramme")
                .put(Stillingskode.class.getName(), "/administrasjon/kodeverk/stillingskode")
                .put(Uketimetall.class.getName(), "/administrasjon/kodeverk/uketimetall")

                .put(Organisasjonselement.class.getName(), "/administrasjon/organisasjon/organisasjonselement")

                .put(Rolle.class.getName(), "/administrasjon/fullmakt/rolle")
                .put(Fullmakt.class.getName(), "/administrasjon/fullmakt/fullmakt")

                .put(Sprak.class.getName(), "/felles/kodeverk/sprak")
                .put(Landkode.class.getName(), "/felles/kodeverk/landkode")
                .put(Kjonn.class.getName(), "/felles/kodeverk/kjonn")
                .put("no.fint.model.utdanning.elev.Elev", "/utdanning/elev/elev")
                .build();
    }

}
