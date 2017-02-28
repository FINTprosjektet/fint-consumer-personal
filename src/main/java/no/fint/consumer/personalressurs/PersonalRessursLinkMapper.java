package no.fint.consumer.personalressurs;

import no.fint.personal.Personalressurs;
import no.fint.relation.model.Relation;
import no.fint.relations.FintLinkMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class PersonalRessursLinkMapper implements FintLinkMapper {

    @Override
    public Link createRelation(Relation relation) {
        return new Link("http://localhost:8080/arbeidsforhold", "arbeidsforhold");
    }

    @Override
    public Class<?> type() {
        return Personalressurs.class;
    }
}
