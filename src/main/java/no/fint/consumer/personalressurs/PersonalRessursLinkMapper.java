package no.fint.consumer.personalressurs;

import no.fint.consumer.FintPersonalProps;
import no.fint.consumer.relation.RelationCacheService;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.relation.Relation;
import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@FintLinkMapper(Personalressurs.class)
@Component
public class PersonalRessursLinkMapper {

    @Autowired
    private FintPersonalProps fintPersonalProps;

    @Autowired
    private RelationCacheService relationCacheService;

    @FintLinkRelation("REL_ID_ARBEIDSFORHOLD")
    public List<Link> createArbeidsforholdRelation(Relation relation) {
        List<String> relatedValues = relationCacheService.getKey(relation.getType(), relation.getMain());
        return relatedValues.stream().map(related -> new Link(
                fintPersonalProps.getLinkMapperBaseUrl() + "/arbeidsforhold/" + related,
                relation.getType())).collect(Collectors.toList());
    }

    @FintLinkRelation("REL_ID_PERSON")
    public List<Link> createPersonRelation(Relation relation) {
        List<String> relatedValues = relationCacheService.getKey(relation.getType(), relation.getMain());
        return relatedValues.stream().map(related -> new Link(
                fintPersonalProps.getLinkMapperBaseUrl() + "/person/" + related,
                relation.getType())).collect(Collectors.toList());
    }

}
