package no.fint.consumer.models.arbeidsforhold;

import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class ArbeidsforholdLinker extends FintLinker<ArbeidsforholdResource> {

    public ArbeidsforholdLinker() {
        super(ArbeidsforholdResource.class);
    }

    public void mapLinks(ArbeidsforholdResource resource) {
        super.mapLinks(resource);
    }
    

    @Override
    public String getSelfHref(ArbeidsforholdResource arbeidsforhold) {
        return createHrefWithId(arbeidsforhold.getSystemId().getIdentifikatorverdi(), "systemid");
    }
    
    
    
}

