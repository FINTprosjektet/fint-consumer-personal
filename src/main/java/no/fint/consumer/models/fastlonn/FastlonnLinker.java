package no.fint.consumer.models.fastlonn;

import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class FastlonnLinker extends FintLinker<FastlonnResource> {

    public FastlonnLinker() {
        super(FastlonnResource.class);
    }

    public void mapLinks(FastlonnResource resource) {
        super.mapLinks(resource);
    }
    

    @Override
    public String getSelfHref(FastlonnResource fastlonn) {
        return createHrefWithId(fastlonn.getSystemId().getIdentifikatorverdi(), "systemid");
    }
    
    
    
}

