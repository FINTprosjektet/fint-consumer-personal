package no.fint.consumer.arbeidsforhold;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.EqualsAndHashCode;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import org.springframework.hateoas.ResourceSupport;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArbeidsforholdResource extends ResourceSupport {
    @JsonUnwrapped
    private Arbeidsforhold arbeidsforhold;
}
