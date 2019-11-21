package no.fint.consumer.models.fravar;

import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.FravarResource;
import no.fint.model.resource.administrasjon.personal.FravarResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;


@Component
public class FravarLinker extends FintLinker<FravarResource> {

    public FravarLinker() {
        super(FravarResource.class);
    }

    public void mapLinks(FravarResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public FravarResources toResources(Collection<FravarResource> collection) {
        FravarResources resources = new FravarResources();
        collection.stream().map(this::toResource).forEach(resources::addResource);
        resources.addSelf(Link.with(self()));
        return resources;
    }

    @Override
    public String getSelfHref(FravarResource fravar) {
        if (!isNull(fravar.getSystemId()) && !isEmpty(fravar.getSystemId().getIdentifikatorverdi())) {
            return createHrefWithId(fravar.getSystemId().getIdentifikatorverdi(), "systemid");
        }
        
        return null;
    }
    
}

