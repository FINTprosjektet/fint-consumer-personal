package no.fint.consumer.personalressurs;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.EqualsAndHashCode;
import no.fint.model.administrasjon.personal.Personalressurs;
import org.springframework.hateoas.ResourceSupport;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonalressursResource extends ResourceSupport {
    @JsonUnwrapped
    private Personalressurs personalressurs;
}
