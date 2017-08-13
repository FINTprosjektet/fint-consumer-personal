package no.fint.consumer.arbeidsforhold;

import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.relation.FintResource;
import no.fint.relations.FintResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class ArbeidsforholdAssembler extends FintResourceAssembler<Arbeidsforhold, ArbeidsforholdResource> {

    public ArbeidsforholdAssembler() {
        super(ArbeidsforholdController.class, ArbeidsforholdResource.class);
    }

    @Override
    public ArbeidsforholdResource mapToResource(FintResource<Arbeidsforhold> resource) {
        Arbeidsforhold arbeidsforhold = resource.getResource();
        ArbeidsforholdResource arbeidsforholdResource = createResourceWithId(arbeidsforhold.getSystemId().getIdentifikatorverdi(), resource, "systemId");
        arbeidsforholdResource.setArbeidsforhold(arbeidsforhold);
        return arbeidsforholdResource;
    }
}
