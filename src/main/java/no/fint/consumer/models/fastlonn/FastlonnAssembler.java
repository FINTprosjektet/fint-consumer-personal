package no.fint.consumer.models.fastlonn;

import no.fint.model.administrasjon.personal.Fastlonn;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import no.fint.relations.FintResourceSupport;
import org.springframework.stereotype.Component;

@Component
public class FastlonnAssembler extends FintResourceAssembler<Fastlonn> {

    public FastlonnAssembler() {
        super(FastlonnController.class);
    }


    @Override
    public FintResourceSupport assemble(Fastlonn fastlonn , FintResource<Fastlonn> fintResource) {
        return createResourceWithId(fastlonn.getSystemId().getIdentifikatorverdi(), fintResource, "systemid");
    }
    
    
    
}

