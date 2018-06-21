package no.fint.consumer.models.personalressurs;

import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

@Component
public class PersonalressursLinker extends FintLinker<PersonalressursResource> {

    public PersonalressursLinker() {
        super(PersonalressursResource.class);
    }

    public void mapLinks(PersonalressursResource resource) {
        super.mapLinks(resource);
    }
    
    @Override
    public String getSelfHref(PersonalressursResource personalressurs) {
        if (personalressurs.getAnsattnummer() != null && personalressurs.getAnsattnummer().getIdentifikatorverdi() != null) {
            return createHrefWithId(personalressurs.getAnsattnummer().getIdentifikatorverdi(), "ansattnummer");
        }
        if (personalressurs.getBrukernavn() != null && personalressurs.getBrukernavn().getIdentifikatorverdi() != null) {
            return createHrefWithId(personalressurs.getBrukernavn().getIdentifikatorverdi(), "brukernavn");
        }
        if (personalressurs.getSystemId() != null && personalressurs.getSystemId().getIdentifikatorverdi() != null) {
            return createHrefWithId(personalressurs.getSystemId().getIdentifikatorverdi(), "systemid");
        }
        
        return null;
    }
    
}

