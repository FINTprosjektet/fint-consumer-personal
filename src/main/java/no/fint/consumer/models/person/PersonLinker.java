package no.fint.consumer.models.person;

import no.fint.model.resource.felles.PersonResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class PersonLinker extends FintLinker<PersonResource> {

    public PersonLinker() {
        super(PersonResource.class);
    }

    public void mapLinks(PersonResource resource) {
        super.mapLinks(resource);
    }
    
    @Override
    public String getSelfHref(PersonResource person) {
        if (person.getFodselsnummer() != null && person.getFodselsnummer().getIdentifikatorverdi() != null) {
            return createHrefWithId(person.getFodselsnummer().getIdentifikatorverdi(), "fodselsnummer");
        }
        
        return null;
    }
    
}

