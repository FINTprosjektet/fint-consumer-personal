package no.fint.data;

import com.google.common.collect.ImmutableList;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArbeidsforholdService {

    public List<FintResource<Arbeidsforhold>> getAll() {
        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi("100_1");

        Arbeidsforhold arbeidsforhold = new Arbeidsforhold();
        arbeidsforhold.setSystemId(systemId);
        arbeidsforhold.setArslonn(300000);
        arbeidsforhold.setAnsettelsesprosent(70);
        arbeidsforhold.setLonnsprosent(80);
        arbeidsforhold.setHovedstilling(true);

        FintResource<Arbeidsforhold> fintResource = FintResource.with(arbeidsforhold).addRelations(
                new Relation.Builder()
                        .with(Arbeidsforhold.Relasjonsnavn.PERSONALRESSURS)
                        .forType(Personalressurs.class)
                        .field("ansattnummer")
                        .value("10025")
                        .build()
        );
        return ImmutableList.of(fintResource);
    }
}
