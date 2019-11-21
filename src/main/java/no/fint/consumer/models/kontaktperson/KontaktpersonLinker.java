package no.fint.consumer.models.kontaktperson;

import no.fint.model.resource.Link;
import no.fint.model.resource.felles.KontaktpersonResource;
import no.fint.model.resource.felles.KontaktpersonResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;


@Component
public class KontaktpersonLinker extends FintLinker<KontaktpersonResource> {

    public KontaktpersonLinker() {
        super(KontaktpersonResource.class);
    }

    public void mapLinks(KontaktpersonResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public KontaktpersonResources toResources(Collection<KontaktpersonResource> collection) {
        KontaktpersonResources resources = new KontaktpersonResources();
        collection.stream().map(this::toResource).forEach(resources::addResource);
        resources.addSelf(Link.with(self()));
        return resources;
    }

    @Override
    public String getSelfHref(KontaktpersonResource kontaktperson) {
        if (!isNull(kontaktperson.getSystemId()) && !isEmpty(kontaktperson.getSystemId().getIdentifikatorverdi())) {
            return createHrefWithId(kontaktperson.getSystemId().getIdentifikatorverdi(), "systemid");
        }
        
        return null;
    }
    
}

