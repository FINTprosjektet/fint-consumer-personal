package no.fint.consumer.models.fasttillegg;

import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import no.fint.model.resource.administrasjon.personal.FasttilleggResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class FasttilleggLinker extends FintLinker<FasttilleggResource> {

    public FasttilleggLinker() {
        super(FasttilleggResource.class);
    }

    public void mapLinks(FasttilleggResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public FasttilleggResources toResources(Collection<FasttilleggResource> collection) {
        FasttilleggResources resources = new FasttilleggResources();
        collection.stream().map(this::toResource).forEach(resources::addResource);
        resources.addSelf(Link.with(self()));
        return resources;
    }

    @Override
    public String getSelfHref(FasttilleggResource fasttillegg) {
        return getAllSelfHrefs(fasttillegg).findFirst().orElse(null);
    }

    @Override
    public Stream<String> getAllSelfHrefs(FasttilleggResource fasttillegg) {
        Stream.Builder<String> builder = Stream.builder();
        if (!isNull(fasttillegg.getKildesystemId()) && !isEmpty(fasttillegg.getKildesystemId().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(fasttillegg.getKildesystemId().getIdentifikatorverdi(), "kildesystemid"));
        }
        if (!isNull(fasttillegg.getSystemId()) && !isEmpty(fasttillegg.getSystemId().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(fasttillegg.getSystemId().getIdentifikatorverdi(), "systemid"));
        }
        
        return builder.build();
    }

    int[] hashCodes(FasttilleggResource fasttillegg) {
        IntStream.Builder builder = IntStream.builder();
        if (!isNull(fasttillegg.getKildesystemId()) && !isEmpty(fasttillegg.getKildesystemId().getIdentifikatorverdi())) {
            builder.add(fasttillegg.getKildesystemId().getIdentifikatorverdi().hashCode());
        }
        if (!isNull(fasttillegg.getSystemId()) && !isEmpty(fasttillegg.getSystemId().getIdentifikatorverdi())) {
            builder.add(fasttillegg.getSystemId().getIdentifikatorverdi().hashCode());
        }
        
        return builder.build().toArray();
    }

}

