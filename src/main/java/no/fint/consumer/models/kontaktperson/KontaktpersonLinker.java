package no.fint.consumer.models.kontaktperson;

import no.fint.model.resource.felles.KontaktpersonResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class KontaktpersonLinker extends FintLinker<KontaktpersonResource> {

    public KontaktpersonLinker() {
        super(KontaktpersonResource.class);
    }


    @Override
    public String getSelfHref(KontaktpersonResource kontaktperson) {
        return createHrefWithId(kontaktperson.getSystemId().getIdentifikatorverdi(), "systemid");
    }
    
    
    
}

