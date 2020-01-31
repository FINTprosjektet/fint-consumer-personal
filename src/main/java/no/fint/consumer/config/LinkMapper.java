package no.fint.consumer.config;

import com.google.common.collect.ImmutableMap;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.model.administrasjon.arkiv.*;
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
                .put(Personalmappe.class.getName(), contextPath + RestEndpoints.PERSONALMAPPE)
                .put(Art.class.getName(), "/administrasjon/kodeverk/art")
                .put(Ansvar.class.getName(), "/administrasjon/kodeverk/ansvar")
                .put(Funksjon.class.getName(), "/administrasjon/kodeverk/funksjon")
                .put(Prosjekt.class.getName(), "/administrasjon/kodeverk/prosjekt")
                .put(Lonnsart.class.getName(), "/administrasjon/kodeverk/lonnsart")
                .put(Fravarsgrunn.class.getName(), "/administrasjon/kodeverk/fravarsgrunn")
                .put(Fravarstype.class.getName(), "/administrasjon/kodeverk/fravarstype")
                .put(Arbeidsforholdstype.class.getName(), "/administrasjon/kodeverk/arbeidsforholdstype")
                .put(Stillingskode.class.getName(), "/administrasjon/kodeverk/stillingskode")
                .put(Uketimetall.class.getName(), "/administrasjon/kodeverk/uketimetall")
                .put(Organisasjonselement.class.getName(), "/administrasjon/organisasjon/organisasjonselement")
                .put(Personalressurskategori.class.getName(), "/administrasjon/kodeverk/personalressurskategori")
                .put(Rolle.class.getName(), "/administrasjon/fullmakt/rolle")
                .put(Fullmakt.class.getName(), "/administrasjon/fullmakt/fullmakt")
                .put(Sprak.class.getName(), "/felles/kodeverk/sprak")
                .put(Landkode.class.getName(), "/felles/kodeverk/landkode")
                .put(Kjonn.class.getName(), "/felles/kodeverk/kjonn")
                .put("no.fint.model.utdanning.elev.Elev", "/utdanning/elev/elev")

                .put(AdministrativEnhet.class.getName(), "/administrasjon/arkiv/administrativenhet")
                .put(Arkivdel.class.getName(), "/administrasjon/arkiv/arkivdel")
                .put(Arkivressurs.class.getName(), "/administrasjon/arkiv/arkivressurs")
                .put(Autorisasjon.class.getName(), "/administrasjon/arkiv/autorisasjon")
                .put(Dokumentfil.class.getName(), "/administrasjon/arkiv/dokumentfil")
                .put(DokumentStatus.class.getName(), "/administrasjon/arkiv/dokumentstatus")
                .put(DokumentType.class.getName(), "/administrasjon/arkiv/dokumenttype")
                .put(JournalpostType.class.getName(), "/administrasjon/arkiv/journalposttype")
                .put(JournalStatus.class.getName(), "/administrasjon/arkiv/journalstatus")
                .put(Klasse.class.getName(), "/administrasjon/arkiv/klasse")
                .put(Klassifikasjonssystem.class.getName(), "/administrasjon/arkiv/klassifikasjonssystem")
                .put(Korrespondansepart.class.getName(), "/administrasjon/arkiv/korrespondansepart")
                .put(KorrespondansepartType.class.getName(), "/administrasjon/arkiv/korrespondanseparttype")
                .put(Merknadstype.class.getName(), "/administrasjon/arkiv/merknadstype")
                .put(Part.class.getName(), "/administrasjon/arkiv/part")
                .put(PartRolle.class.getName(), "/administrasjon/arkiv/partrolle")
                .put(no.fint.model.administrasjon.arkiv.Rolle.class.getName(), "/administrasjon/arkiv/rolle")
                .put(Sak.class.getName(), "/administrasjon/arkiv/sak")
                .put(Saksstatus.class.getName(), "/administrasjon/arkiv/saksstatus")
                .put(TilknyttetRegistreringSom.class.getName(), "/administrasjon/arkiv/tilknyttetregistreringsom")
                .put(Skjermingshjemmel.class.getName(), "/administrasjon/arkiv/skjermingshjemmel")
                .put(Tilgang.class.getName(), "/administrasjon/arkiv/tilgang")
                .put(Tilgangsrestriksjon.class.getName(), "/administrasjon/arkiv/tilgangsrestriksjon")
                .put(Variantformat.class.getName(), "/administrasjon/arkiv/variantformat")

                .build();
    }

}
