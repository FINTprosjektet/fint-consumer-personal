package no.fint.consumer.arbeidsforhold;

import no.fint.consumer.FintPersonalProps;
import no.fint.consumer.relation.RelationCacheService;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.relation.Relation;
import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@FintLinkMapper(Arbeidsforhold.class)
@Component
public class ArbeidsforholdLinkMapper {

    @Autowired
    private FintPersonalProps fintPersonalProps;

    @Autowired
    private RelationCacheService relationCacheService;

    @FintLinkRelation("REL_ID_PERSONALRESSURS")
    public List<Link> createRelation(Relation relation) {
        List<String> rightKeys = relationCacheService.getKey(relation.getType(), relation.getMain());
        return rightKeys.stream().map(rightKey -> new Link(
                fintPersonalProps.getLinkMapperBaseUrl() + "/administrasjon/personal/personalressurs/" + rightKey,
                "personalressurs")).collect(Collectors.toList());
    }

}
