package no.fint.consumer.person;

import no.fint.consumer.relation.RelationCacheService;
import no.fint.felles.Person;
import no.fint.relation.model.Relation;
import no.fint.relations.FintLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersonLinkMapper implements FintLinkMapper {

    @Autowired
    private RelationCacheService relationCacheService;

    @Override
    public Link createRelation(Relation relation) {
        Optional<String> rightKey = relationCacheService.getKey(relation.getType(), relation.getLeftKey());
        if (rightKey.isPresent()) {
            return new Link("http://localhost:8080/administrasjon/personal/personalressurs/" + rightKey.get(), "personalressurs");
        }
        return null;
    }

    @Override
    public Class<?> type() {
        return Person.class;
    }
}
