package no.fint.consumer.test;

import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.event.EventListener;
import no.fint.event.model.Event;
import no.fint.model.administrasjon.kodeverk.Fravarsgrunn;
import no.fint.model.administrasjon.kodeverk.Fravarstype;
import no.fint.model.administrasjon.kompleksedatatyper.Beskjeftigelse;
import no.fint.model.administrasjon.kompleksedatatyper.Fasttillegg;
import no.fint.model.administrasjon.kompleksedatatyper.Kontostreng;
import no.fint.model.administrasjon.kompleksedatatyper.Variabelttillegg;
import no.fint.model.administrasjon.personal.*;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Profile("test")
public class TestData {

    @Autowired
    EventListener eventListener;

    @PostConstruct
    public void init() {
        eventListener.accept(createEvent(PersonalActions.GET_ALL_FRAVAR, Collections.singletonList(fakeFravar())));
        eventListener.accept(createEvent(PersonalActions.GET_ALL_FASTLONN, Collections.singletonList(fakeFastlonn())));
        eventListener.accept(createEvent(PersonalActions.GET_ALL_VARIABELLONN, Collections.singletonList(fakeVariabellonn())));
        log.info("Alternative facts restored for mock.no");
    }

    private Event createEvent(Enum action, List list) {
        Event result = new Event();
        result.setCorrId(UUID.randomUUID().toString());
        result.setAction(action);
        result.setOrgId("mock.no");
        result.setData(list);
        return result;
    }

    public static FintResource<Variabellonn> fakeVariabellonn() {
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(UUID.randomUUID().toString());

        Variabelttillegg variabelttillegg = new Variabelttillegg();
        variabelttillegg.setAntall(1045L);
        variabelttillegg.setBeskrivelse("Kjøregodtgjørelse");
        variabelttillegg.setPeriode(getPeriode());

        Variabellonn variabellonn = new Variabellonn();
        variabellonn.setSystemId(identifikator);
        variabellonn.setAttestert(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
        variabellonn.setAnvist(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(5)));
        variabellonn.setPeriode(getPeriode());
        variabellonn.setVariabelttillegg(Collections.singletonList(variabelttillegg));

        return FintResource.with(variabellonn)
                .addRelations(new Relation.Builder().with(Fastlonn.Relasjonsnavn.ARBEIDSFORHOLD).forType(Arbeidsforhold.class).field("systemid").value("1234").build())
                .addRelations(new Relation.Builder().with(Fastlonn.Relasjonsnavn.ATTESTANT).forType(Personalressurs.class).field("ansattnummer").value("100000").build())
                .addRelations(new Relation.Builder().with(Fastlonn.Relasjonsnavn.ANVISER).forType(Personalressurs.class).field("ansattnummer").value("100001").build());
    }

    private static Periode getPeriode() {
        Periode periode = new Periode();
        periode.setStart(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10)));
        periode.setSlutt(new Date());
        return periode;
    }

    public static FintResource<Fastlonn> fakeFastlonn() {
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(UUID.randomUUID().toString());

        Beskjeftigelse beskjeftigelse = new Beskjeftigelse();
        beskjeftigelse.setProsent(10000L);
        beskjeftigelse.setBeskrivelse("1STA KLST");
        beskjeftigelse.setKontostreng(new Kontostreng());
        beskjeftigelse.setPeriode(getPeriode());

        Fasttillegg fasttillegg = new Fasttillegg();
        fasttillegg.setBelop(100000L);
        fasttillegg.setBeskrivelse("Kontaktlærertillegg");
        fasttillegg.setKontostreng(new Kontostreng());
        fasttillegg.setPeriode(getPeriode());

        Fastlonn fastlonn = new Fastlonn();
        fastlonn.setSystemId(identifikator);
        fastlonn.setBeskjeftigelse(Collections.singletonList(beskjeftigelse));
        fastlonn.setFasttillegg(Collections.singletonList(fasttillegg));
        fastlonn.setAttestert(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
        fastlonn.setAnvist(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(5)));
        fastlonn.setPeriode(getPeriode());

        return FintResource.with(fastlonn)
                .addRelations(new Relation.Builder().with(Fastlonn.Relasjonsnavn.ARBEIDSFORHOLD).forType(Arbeidsforhold.class).field("systemid").value("1234").build())
                .addRelations(new Relation.Builder().with(Fastlonn.Relasjonsnavn.ATTESTANT).forType(Personalressurs.class).field("ansattnummer").value("100000").build())
                .addRelations(new Relation.Builder().with(Fastlonn.Relasjonsnavn.ANVISER).forType(Personalressurs.class).field("ansattnummer").value("100001").build());

    }

    public static FintResource<Fravar> fakeFravar() {
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(UUID.randomUUID().toString());

        Periode periode = new Periode();
        periode.setStart(new Date(2018 - 1900, 1, 1));
        periode.setSlutt(new Date());

        Fravar fravar = new Fravar();
        fravar.setProsent(10000L);
        fravar.setSystemId(identifikator);
        fravar.setPeriode(periode);

        return FintResource.with(fravar)
                .addRelations(new Relation.Builder()
                        .with(Fravar.Relasjonsnavn.ARBEIDSFORHOLD)
                        .forType(Arbeidsforhold.class)
                        .field("ansattnummer")
                        .value("12345").build())
                .addRelations(new Relation.Builder()
                        .with(Fravar.Relasjonsnavn.FRAVARSGRUNN)
                        .forType(Fravarsgrunn.class)
                        .field("systemid")
                        .value("111").build())
                .addRelations(new Relation.Builder()
                        .with(Fravar.Relasjonsnavn.FRAVARSTYPE)
                        .forType(Fravarstype.class)
                        .field("systemid")
                        .value("222").build());

    }
}
