package no.fint.consumer.person;

import no.fint.model.felles.Person;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class PersonAssembler extends FintResourceAssembler<Person, PersonResource> {
    public PersonAssembler() {
        super(PersonController.class, PersonResource.class);
    }

    @Override
    public PersonResource mapToResource(FintResource<Person> resource) {
        Person person = resource.getResource();
        PersonResource personResource = createResourceWithId(person.getFodselsnummer().getIdentifikatorverdi(), resource, "fodselsnummer");
        personResource.setPerson(person);
        return personResource;
    }
}
