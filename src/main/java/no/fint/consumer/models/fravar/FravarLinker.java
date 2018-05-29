package no.fint.consumer.models.fravar;

import no.fint.model.resource.administrasjon.personal.FravarResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class FravarLinker extends FintLinker<FravarResource> {

    public FravarLinker() {
        super(FravarResource.class);
    }

    public void mapLinks(FravarResource resource) {
        super.mapLinks(resource);
    }
    

    @Override
    public String getSelfHref(FravarResource fravar) {
        return createHrefWithId(fravar.getSystemId().getIdentifikatorverdi(), "systemid");
    }
    
    
    
}

