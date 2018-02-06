package no.fint.consumer.models.arbeidsforhold;

import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import no.fint.relations.FintResourceSupport;
import org.springframework.stereotype.Component;

@Component
public class ArbeidsforholdAssembler extends FintResourceAssembler<Arbeidsforhold> {

    public ArbeidsforholdAssembler() {
        super(ArbeidsforholdController.class);
    }


    @Override
    public FintResourceSupport assemble(Arbeidsforhold arbeidsforhold , FintResource<Arbeidsforhold> fintResource) {
        return createResourceWithId(arbeidsforhold.getSystemId().getIdentifikatorverdi(), fintResource, "systemid");
    }
    
    
}

