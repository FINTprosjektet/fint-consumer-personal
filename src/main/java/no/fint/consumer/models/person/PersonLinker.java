package no.fint.consumer.models.person;

import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.PersonResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;


@Component
public class PersonLinker extends FintLinker<PersonResource> {

    public PersonLinker() {
        super(PersonResource.class);
    }

    public void mapLinks(PersonResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public PersonResources toResources(Collection<PersonResource> collection) {
        PersonResources resources = new PersonResources();
        collection.stream().map(this::toResource).forEach(resources::addResource);
        resources.addSelf(Link.with(self()));
        return resources;
    }

    @Override
    public String getSelfHref(PersonResource person) {
        if (!isNull(person.getFodselsnummer()) && !isEmpty(person.getFodselsnummer().getIdentifikatorverdi())) {
            return createHrefWithId(person.getFodselsnummer().getIdentifikatorverdi(), "fodselsnummer");
        }
        
        return null;
    }

    int[] hashCodes(PersonResource person) {
        IntStream.Builder builder = IntStream.builder();
        if (!isNull(person.getFodselsnummer()) && !isEmpty(person.getFodselsnummer().getIdentifikatorverdi())) {
            builder.add(person.getFodselsnummer().getIdentifikatorverdi().hashCode());
        }
        
        return builder.build().toArray();
    }

}

