package no.fint.consumer.models.fasttillegg;

import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class FasttilleggLinker extends FintLinker<FasttilleggResource> {

    public FasttilleggLinker() {
        super(FasttilleggResource.class);
    }

    public void mapLinks(FasttilleggResource resource) {
        super.mapLinks(resource);
    }
    
    @Override
    public String getSelfHref(FasttilleggResource fasttillegg) {
        if (fasttillegg.getSystemId() != null && fasttillegg.getSystemId().getIdentifikatorverdi() != null) {
            return createHrefWithId(fasttillegg.getSystemId().getIdentifikatorverdi(), "systemid");
        }
        
        return null;
    }
    
}

