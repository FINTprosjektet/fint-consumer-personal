package no.fint.consumer.personalressurs;

import no.fint.consumer.relation.RelationCacheService;
import no.fint.felles.Person;
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

@FintLinkMapper
@Component
public class PersonalRessursLinkMapper {

    @Value("${link-mapper-base-url:http://localhost:8080}")
    private String baseUrl;

    @Autowired
    private RelationCacheService relationCacheService;

    @FintLinkRelation(leftObject = Personalressurs.class, leftId = "ansattnummer.identifikatorverdi", rightObject = Arbeidsforhold.class, rightId = "stillingsnummer")
    public List<Link> createArbeidsforholdRelation(Relation relation) {
        List<String> rightKeys = relationCacheService.getKey(relation.getType(), relation.getLeftKey());
        return rightKeys.stream().map(rightKey -> new Link(baseUrl + "/administrasjon/personal/arbeidsforhold/" + rightKey)).collect(Collectors.toList());
    }

    @FintLinkRelation(leftObject = Personalressurs.class, leftId = "ansattnummer.identifikatorverdi", rightObject = Person.class, rightId = "foedselsnummer.identifikatorverdi")
    public List<Link> createPersonRelation(Relation relation) {
        List<String> rightKeys = relationCacheService.getKey(relation.getType(), relation.getLeftKey());
        return rightKeys.stream().map(rightKey -> new Link(baseUrl + "/administrasjon/personal/person/" + rightKey)).collect(Collectors.toList());
    }

}
