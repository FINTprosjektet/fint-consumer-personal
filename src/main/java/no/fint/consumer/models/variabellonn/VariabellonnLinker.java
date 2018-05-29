package no.fint.consumer.models.variabellonn;

import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class VariabellonnLinker extends FintLinker<VariabellonnResource> {

    public VariabellonnLinker() {
        super(VariabellonnResource.class);
    }

    public void mapLinks(VariabellonnResource resource) {
        super.mapLinks(resource);
    }
    

    @Override
    public String getSelfHref(VariabellonnResource variabellonn) {
        return createHrefWithId(variabellonn.getSystemId().getIdentifikatorverdi(), "systemid");
    }
    
    
    
}

