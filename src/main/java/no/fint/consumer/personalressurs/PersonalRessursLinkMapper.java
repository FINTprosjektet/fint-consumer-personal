package no.fint.consumer.personalressurs;

import no.fint.consumer.relation.RelationCacheService;
import no.fint.personal.Personalressurs;
import no.fint.relation.model.Relation;
import no.fint.relations.FintLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersonalRessursLinkMapper implements FintLinkMapper {

    @Value("${link-mapper-base-url:http://localhost:8080}")
    private String baseUrl;


    @Autowired
    private RelationCacheService relationCacheService;

    @Override
    public Link createRelation(Relation relation) {
        Optional<String> rightKey = relationCacheService.getKey(relation.getType(), relation.getLeftKey());
        if (rightKey.isPresent()) {
            return new Link(baseUrl + "/administrasjon/personal/arbeidsforhold/" + rightKey.get(), "arbeidsforhold");
        }
        return null;
    }

    @Override
    public Class<?> type() {
        return Personalressurs.class;
    }
}
