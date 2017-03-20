package no.fint.consumer.arbeidsforhold;

import no.fint.consumer.relation.RelationCacheService;
import no.fint.personal.Arbeidsforhold;
import no.fint.personal.Personalressurs;
import no.fint.relation.model.Relation;
import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@FintLinkMapper(leftObject = Arbeidsforhold.class, leftId = "stillingsnummer")
@Component
public class ArbeidsforholdLinkMapper {

    @Value("${link-mapper-base-url:http://localhost:8080}")
    private String baseUrl;

    @Autowired
    private RelationCacheService relationCacheService;

    @FintLinkRelation(rightObject = Personalressurs.class, rightId = "ansattnummer.identifikatorverdi")
    public List<Link> createRelation(Relation relation) {
        List<String> rightKeys = relationCacheService.getKey(relation.getType(), relation.getLeftKey());
        return rightKeys.stream().map(rightKey -> new Link(baseUrl + "/administrasjon/personal/personalressurs/" + rightKey)).collect(Collectors.toList());
    }

}
