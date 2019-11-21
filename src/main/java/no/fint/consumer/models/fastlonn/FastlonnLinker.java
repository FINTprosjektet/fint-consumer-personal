package no.fint.consumer.models.fastlonn;

import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import no.fint.model.resource.administrasjon.personal.FastlonnResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;


@Component
public class FastlonnLinker extends FintLinker<FastlonnResource> {

    public FastlonnLinker() {
        super(FastlonnResource.class);
    }

    public void mapLinks(FastlonnResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public FastlonnResources toResources(Collection<FastlonnResource> collection) {
        FastlonnResources resources = new FastlonnResources();
        collection.stream().map(this::toResource).forEach(resources::addResource);
        resources.addSelf(Link.with(self()));
        return resources;
    }

    @Override
    public String getSelfHref(FastlonnResource fastlonn) {
        if (!isNull(fastlonn.getKildesystemId()) && !isEmpty(fastlonn.getKildesystemId().getIdentifikatorverdi())) {
            return createHrefWithId(fastlonn.getKildesystemId().getIdentifikatorverdi(), "kildesystemid");
        }
        if (!isNull(fastlonn.getSystemId()) && !isEmpty(fastlonn.getSystemId().getIdentifikatorverdi())) {
            return createHrefWithId(fastlonn.getSystemId().getIdentifikatorverdi(), "systemid");
        }
        
        return null;
    }
    
}

