package no.fint.consumer.personalressurs;

import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class PersonalressursAssembler extends FintResourceAssembler<Personalressurs, PersonalressursResource> {

    public PersonalressursAssembler() {
        super(PersonalressursController.class, PersonalressursResource.class);
    }

    @Override
    public PersonalressursResource mapToResource(FintResource<Personalressurs> resource) {
        Personalressurs personalressurs = resource.getResource();
        PersonalressursResource personalressursResource = createResourceWithId(personalressurs.getAnsattnummer().getIdentifikatorverdi(), resource, "ansattnummer");
        personalressursResource.setPersonalressurs(personalressurs);
        return personalressursResource;
    }
}
