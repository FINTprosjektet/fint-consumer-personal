package no.fint.consumer.models.person;

import no.fint.model.felles.Person;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import no.fint.relations.FintResourceSupport;
import org.springframework.stereotype.Component;

@Component
public class PersonAssembler extends FintResourceAssembler<Person> {

    public PersonAssembler() {
        super(PersonController.class);
    }


    @Override
    public FintResourceSupport assemble(Person person , FintResource<Person> fintResource) {
        return createResourceWithId(person.getFodselsnummer().getIdentifikatorverdi(), fintResource, "fodselsnummer");
    }
    
    
}

