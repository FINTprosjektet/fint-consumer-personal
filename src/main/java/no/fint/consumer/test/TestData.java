package no.fint.consumer.test;

import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.models.fravar.FravarCacheService;
import no.fint.model.administrasjon.fravar.Fravar;
import no.fint.model.administrasjon.fravar.Fravarsgrunn;
import no.fint.model.administrasjon.fravar.Fravarstype;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
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
import java.util.UUID;

@Slf4j
@Component
@Profile("test")
public class TestData {

    @Autowired
    FravarCacheService fravarCacheService;

    @PostConstruct
    public void init() {
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(UUID.randomUUID().toString());

        Periode periode = new Periode();
        periode.setStart(new Date(2018-1900, 1, 1));
        periode.setSlutt(new Date());

        Fravar fravar = new Fravar();
        fravar.setProsent(10000L);
        fravar.setSystemId(identifikator);
        fravar.setPeriode(periode);

        FintResource resource = FintResource.with(fravar)
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

        fravarCacheService.update("mock.no", Collections.singletonList(resource));
        log.info("Added fake data for mock.no");
    }

}
