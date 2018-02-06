package no.fint.consumer.models.variabellonn;

import no.fint.model.administrasjon.personal.Variabellonn;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import no.fint.relations.FintResourceSupport;
import org.springframework.stereotype.Component;

@Component
public class VariabellonnAssembler extends FintResourceAssembler<Variabellonn> {

    public VariabellonnAssembler() {
        super(VariabellonnController.class);
    }


    @Override
    public FintResourceSupport assemble(Variabellonn variabellonn , FintResource<Variabellonn> fintResource) {
        return createResourceWithId(variabellonn.getSystemId().getIdentifikatorverdi(), fintResource, "systemid");
    }
    
    
}

