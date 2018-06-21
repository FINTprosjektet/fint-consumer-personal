package no.fint.consumer.models.kontaktperson;

import no.fint.model.resource.felles.KontaktpersonResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class KontaktpersonLinker extends FintLinker<KontaktpersonResource> {

    public KontaktpersonLinker() {
        super(KontaktpersonResource.class);
    }

    public void mapLinks(KontaktpersonResource resource) {
        super.mapLinks(resource);
    }
    
    @Override
    public String getSelfHref(KontaktpersonResource kontaktperson) {
        if (kontaktperson.getSystemId() != null && kontaktperson.getSystemId().getIdentifikatorverdi() != null) {
            return createHrefWithId(kontaktperson.getSystemId().getIdentifikatorverdi(), "systemid");
        }
        
        return null;
    }
    
}

