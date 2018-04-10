package no.fint.consumer.models.person;

import no.fint.model.resource.felles.PersonResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class PersonLinker extends FintLinker<PersonResource> {

    public PersonLinker() {
        super(PersonResource.class);
    }


    @Override
    public String getSelfHref(PersonResource person) {
        return createHrefWithId(person.getFodselsnummer().getIdentifikatorverdi(), "fodselsnummer");
    }
    
    
    
}

