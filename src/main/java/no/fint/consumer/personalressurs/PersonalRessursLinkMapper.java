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

@FintLinkMapper(leftObject = Personalressurs.class, leftId = "ansattnummer.identifikatorverdi")
@Component
public class PersonalRessursLinkMapper {

    @Value("${link-mapper-base-url:https://dokumentasjon.felleskomponent.no/relasjoner}")
    private String baseUrl;

    @Autowired
    private RelationCacheService relationCacheService;

    @FintLinkRelation(rightObject = Arbeidsforhold.class, rightId = "stillingsnummer")
    public List<Link> createArbeidsforholdRelation(Relation relation) {
        List<String> rightKeys = relationCacheService.getKey(relation.getType(), relation.getLeftKey());
        return rightKeys.stream().map(rightKey -> new Link(baseUrl + "/administrasjon/personal/arbeidsforhold/" + rightKey, "arbeidsforhold")).collect(Collectors.toList());
    }

    @FintLinkRelation(rightObject = Person.class, rightId = "foedselsnummer.identifikatorverdi")
    public List<Link> createPersonRelation(Relation relation) {
        List<String> rightKeys = relationCacheService.getKey(relation.getType(), relation.getLeftKey());
        return rightKeys.stream().map(rightKey -> new Link(baseUrl + "/administrasjon/personal/person/" + rightKey, "person")).collect(Collectors.toList());
    }

}
