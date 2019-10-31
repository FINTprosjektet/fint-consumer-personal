package no.fint.consumer.models.variabellonn;

import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import no.fint.model.resource.administrasjon.personal.VariabellonnResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;


@Component
public class VariabellonnLinker extends FintLinker<VariabellonnResource> {

    public VariabellonnLinker() {
        super(VariabellonnResource.class);
    }

    public void mapLinks(VariabellonnResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public VariabellonnResources toResources(Collection<VariabellonnResource> collection) {
        VariabellonnResources resources = new VariabellonnResources();
        collection.stream().map(this::toResource).forEach(resources::addResource);
        resources.addSelf(Link.with(self()));
        return resources;
    }

    @Override
    public String getSelfHref(VariabellonnResource variabellonn) {
        if (!isNull(variabellonn.getKildesystemId()) && !isEmpty(variabellonn.getKildesystemId().getIdentifikatorverdi())) {
            return createHrefWithId(variabellonn.getKildesystemId().getIdentifikatorverdi(), "kildesystemid");
        }
        if (!isNull(variabellonn.getSystemId()) && !isEmpty(variabellonn.getSystemId().getIdentifikatorverdi())) {
            return createHrefWithId(variabellonn.getSystemId().getIdentifikatorverdi(), "systemid");
        }
        
        return null;
    }
    
}

