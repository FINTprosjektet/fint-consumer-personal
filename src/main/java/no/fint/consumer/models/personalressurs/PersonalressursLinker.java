package no.fint.consumer.models.personalressurs;

import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class PersonalressursLinker extends FintLinker<PersonalressursResource> {

    public PersonalressursLinker() {
        super(PersonalressursResource.class);
    }


    @Override
    public String getSelfHref(PersonalressursResource personalressurs) {
        return createHrefWithId(personalressurs.getAnsattnummer().getIdentifikatorverdi(), "ansattnummer");
    }
    
    
    
}

