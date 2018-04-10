package no.fint.consumer.models.variabellonn;

import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class VariabellonnLinker extends FintLinker<VariabellonnResource> {

    public VariabellonnLinker() {
        super(VariabellonnResource.class);
    }


    @Override
    public String getSelfHref(VariabellonnResource variabellonn) {
        return createHrefWithId(variabellonn.getSystemId().getIdentifikatorverdi(), "systemid");
    }
    
    
    
}

