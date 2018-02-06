package no.fint.consumer.models.personalressurs;

import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import no.fint.relations.FintResourceSupport;
import org.springframework.stereotype.Component;

@Component
public class PersonalressursAssembler extends FintResourceAssembler<Personalressurs> {

    public PersonalressursAssembler() {
        super(PersonalressursController.class);
    }


    @Override
    public FintResourceSupport assemble(Personalressurs personalressurs , FintResource<Personalressurs> fintResource) {
        return createResourceWithId(personalressurs.getAnsattnummer().getIdentifikatorverdi(), fintResource, "ansattnummer");
    }
    
    
}

