package no.fint.consumer.models.personalressurs;

import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;

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
        if (!isNull(personalressurs.getAnsattnummer()) && !isEmpty(personalressurs.getAnsattnummer().getIdentifikatorverdi())) {
            return createHrefWithId(personalressurs.getAnsattnummer().getIdentifikatorverdi(), "ansattnummer");
        }
        if (!isNull(personalressurs.getBrukernavn()) && !isEmpty(personalressurs.getBrukernavn().getIdentifikatorverdi())) {
            return createHrefWithId(personalressurs.getBrukernavn().getIdentifikatorverdi(), "brukernavn");
        }
        if (!isNull(personalressurs.getSystemId()) && !isEmpty(personalressurs.getSystemId().getIdentifikatorverdi())) {
            return createHrefWithId(personalressurs.getSystemId().getIdentifikatorverdi(), "systemid");
        }
        
        return null;
    }
    
}

