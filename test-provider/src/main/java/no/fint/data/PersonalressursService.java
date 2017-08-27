package no.fint.data;

import com.google.common.collect.ImmutableList;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.felles.Person;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalressursService {

    public List<FintResource<Personalressurs>> getAll() {
        Identifikator ansattnummer = new Identifikator();
        ansattnummer.setIdentifikatorverdi("10025");
        Personalressurs personalressurs = new Personalressurs();
        personalressurs.setAnsattnummer(ansattnummer);

        FintResource<Personalressurs> fintResource = FintResource.with(personalressurs).addRelations(
                new Relation.Builder().with(Personalressurs.Relasjonsnavn.PERSON).forType(Person.class).field("fodselsnummer").value("204194497763").build()
        );

        return ImmutableList.of(fintResource);
    }
}
