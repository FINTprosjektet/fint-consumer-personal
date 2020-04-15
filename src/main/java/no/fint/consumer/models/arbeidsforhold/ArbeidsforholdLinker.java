package no.fint.consumer.models.arbeidsforhold;

import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        return getAllSelfHrefs(arbeidsforhold).findFirst().orElse(null);
    }

    @Override
    public Stream<String> getAllSelfHrefs(ArbeidsforholdResource arbeidsforhold) {
        Stream.Builder<String> builder = Stream.builder();
        if (!isNull(arbeidsforhold.getSystemId()) && !isEmpty(arbeidsforhold.getSystemId().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(arbeidsforhold.getSystemId().getIdentifikatorverdi(), "systemid"));
        }

        return builder.build();
    }

    int[] hashCodes(ArbeidsforholdResource arbeidsforhold) {
        IntStream.Builder builder = IntStream.builder();
        if (!isNull(arbeidsforhold.getSystemId()) && !isEmpty(arbeidsforhold.getSystemId().getIdentifikatorverdi())) {
            builder.add(arbeidsforhold.getSystemId().getIdentifikatorverdi().hashCode());
        }

        return builder.build().toArray();
    }

    public ArbeidsforholdResources toResources(Stream<ArbeidsforholdResource> arbeidsforhold, int offset, int size, int totalItems) {
        ArbeidsforholdResources resources = new ArbeidsforholdResources();
        arbeidsforhold.map(this::toResource).forEach(resources::addResource);
        resources.addSelf(
                Link.with(
                        UriComponentsBuilder
                                .fromUriString(self())
                                .queryParam("offset", offset)
                                .queryParam("size", size)
                                .toUriString()));
        if (offset >= size) {
            resources.addPrev(
                    Link.with(
                            UriComponentsBuilder
                                    .fromUriString(self())
                                    .queryParam("offset", offset - size)
                                    .queryParam("size", size)
                                    .toUriString()));
        }
        if (offset + size < totalItems) {
            resources.addNext(
                    Link.with(
                            UriComponentsBuilder
                                    .fromUriString(self())
                                    .queryParam("offset", offset + size)
                                    .queryParam("size", size)
                                    .toUriString()));
        }
        resources.setOffset(offset);
        resources.setTotalItems(totalItems);
        return resources;
    }
}

