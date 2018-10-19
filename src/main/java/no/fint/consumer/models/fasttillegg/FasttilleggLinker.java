package no.fint.consumer.models.fasttillegg;

import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import no.fint.model.resource.administrasjon.personal.FasttilleggResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;


@Component
public class FasttilleggLinker extends FintLinker<FasttilleggResource> {

    public FasttilleggLinker() {
        super(FasttilleggResource.class);
    }

    public void mapLinks(FasttilleggResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public FasttilleggResources toResources(Collection<FasttilleggResource> collection) {
        FasttilleggResources resources = new FasttilleggResources();
        collection.stream().map(this::toResource).forEach(resources::addResource);
        resources.addSelf(Link.with(self()));
        return resources;
    }

    @Override
    public String getSelfHref(FasttilleggResource fasttillegg) {
        if (!isNull(fasttillegg.getSystemId()) && !isEmpty(fasttillegg.getSystemId().getIdentifikatorverdi())) {
            return createHrefWithId(fasttillegg.getSystemId().getIdentifikatorverdi(), "systemid");
        }
        
        return null;
    }
    
}

