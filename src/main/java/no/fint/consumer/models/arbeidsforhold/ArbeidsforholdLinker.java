package no.fint.consumer.models.arbeidsforhold;

import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;


@Component
public class ArbeidsforholdLinker extends FintLinker<ArbeidsforholdResource> {

    public ArbeidsforholdLinker() {
        super(ArbeidsforholdResource.class);
    }

    public void mapLinks(ArbeidsforholdResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public ArbeidsforholdResources toResources(Collection<ArbeidsforholdResource> collection) {
        ArbeidsforholdResources resources = new ArbeidsforholdResources();
        collection.stream().map(this::toResource).forEach(resources::addResource);
        resources.addSelf(Link.with(self()));
        return resources;
    }

    @Override
    public String getSelfHref(ArbeidsforholdResource arbeidsforhold) {
        if (!isNull(arbeidsforhold.getSystemId()) && !isEmpty(arbeidsforhold.getSystemId().getIdentifikatorverdi())) {
            return createHrefWithId(arbeidsforhold.getSystemId().getIdentifikatorverdi(), "systemid");
        }
        
        return null;
    }

    int[] hashCodes(ArbeidsforholdResource arbeidsforhold) {
        IntStream.Builder builder = IntStream.builder();
        if (!isNull(arbeidsforhold.getSystemId()) && !isEmpty(arbeidsforhold.getSystemId().getIdentifikatorverdi())) {
            builder.add(arbeidsforhold.getSystemId().getIdentifikatorverdi().hashCode());
        }
        
        return builder.build().toArray();
    }

}

