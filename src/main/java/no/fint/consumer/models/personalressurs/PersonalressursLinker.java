package no.fint.consumer.models.personalressurs;

import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class PersonalressursLinker extends FintLinker<PersonalressursResource> {

    public PersonalressursLinker() {
        super(PersonalressursResource.class);
    }

    public void mapLinks(PersonalressursResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public PersonalressursResources toResources(Collection<PersonalressursResource> collection) {
        PersonalressursResources resources = new PersonalressursResources();
        collection.stream().map(this::toResource).forEach(resources::addResource);
        resources.addSelf(Link.with(self()));
        return resources;
    }

    @Override
    public String getSelfHref(PersonalressursResource personalressurs) {
        return getAllSelfHrefs(personalressurs).findFirst().orElse(null);
    }

    @Override
    public Stream<String> getAllSelfHrefs(PersonalressursResource personalressurs) {
        Stream.Builder<String> builder = Stream.builder();
        if (!isNull(personalressurs.getAnsattnummer()) && !isEmpty(personalressurs.getAnsattnummer().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(personalressurs.getAnsattnummer().getIdentifikatorverdi(), "ansattnummer"));
        }
        if (!isNull(personalressurs.getBrukernavn()) && !isEmpty(personalressurs.getBrukernavn().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(personalressurs.getBrukernavn().getIdentifikatorverdi(), "brukernavn"));
        }
        if (!isNull(personalressurs.getSystemId()) && !isEmpty(personalressurs.getSystemId().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(personalressurs.getSystemId().getIdentifikatorverdi(), "systemid"));
        }
        
        return builder.build();
    }

    int[] hashCodes(PersonalressursResource personalressurs) {
        IntStream.Builder builder = IntStream.builder();
        if (!isNull(personalressurs.getAnsattnummer()) && !isEmpty(personalressurs.getAnsattnummer().getIdentifikatorverdi())) {
            builder.add(personalressurs.getAnsattnummer().getIdentifikatorverdi().hashCode());
        }
        if (!isNull(personalressurs.getBrukernavn()) && !isEmpty(personalressurs.getBrukernavn().getIdentifikatorverdi())) {
            builder.add(personalressurs.getBrukernavn().getIdentifikatorverdi().hashCode());
        }
        if (!isNull(personalressurs.getSystemId()) && !isEmpty(personalressurs.getSystemId().getIdentifikatorverdi())) {
            builder.add(personalressurs.getSystemId().getIdentifikatorverdi().hashCode());
        }
        
        return builder.build().toArray();
    }

}

