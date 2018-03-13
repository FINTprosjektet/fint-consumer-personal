package no.fint.consumer.models.fravar;

import no.fint.model.administrasjon.personal.Fravar;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import no.fint.relations.FintResourceSupport;
import org.springframework.stereotype.Component;

@Component
public class FravarAssembler extends FintResourceAssembler<Fravar> {

    public FravarAssembler() {
        super(FravarController.class);
    }


    @Override
    public FintResourceSupport assemble(Fravar fravar , FintResource<Fravar> fintResource) {
        return createResourceWithId(fravar.getSystemId().getIdentifikatorverdi(), fintResource, "systemid");
    }
    
    
    
}

