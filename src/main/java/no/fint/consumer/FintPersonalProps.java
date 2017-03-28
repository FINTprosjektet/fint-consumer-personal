package no.fint.consumer;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FintPersonalProps {

    @Getter
    @Value("${link-mapper-base-url:https://api.felleskomponent.no/administrasjon/personal}")
    private String linkMapperBaseUrl;


}
