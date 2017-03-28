package no.fint.consumer;

import com.github.springfox.loader.EnableSpringfox;
import no.fint.audit.EnableFintAudit;
import no.fint.events.EnableFintEvents;
import no.fint.relations.annotations.EnableFintRelations;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFintRelations
@EnableFintEvents
@EnableFintAudit
@EnableScheduling
@EnableSpringfox
@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
