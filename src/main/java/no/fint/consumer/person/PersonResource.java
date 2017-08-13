package no.fint.consumer.person;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.EqualsAndHashCode;
import no.fint.model.felles.Person;
import org.springframework.hateoas.ResourceSupport;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonResource extends ResourceSupport {
    @JsonUnwrapped
    private Person person;
}
