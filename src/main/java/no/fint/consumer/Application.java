package no.fint.consumer;

import com.github.springfox.loader.EnableSpringfox;
import io.swagger.annotations.*;
import no.fint.audit.EnableFintAudit;
import no.fint.dependencies.annotations.EnableFintDependencies;
import no.fint.events.annotations.EnableFintEvents;
import no.fint.relations.annotations.EnableFintRelations;
import no.fint.springfox.EnableSpringfoxExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFintDependencies
@EnableFintRelations
@EnableFintEvents
@EnableFintAudit
@EnableScheduling
@EnableSpringfox(@Info(title = "FINT Consumer Personal", version = "${fint.version}",
        extensions = {@Extension(name = "x-logo",
                properties = {@ExtensionProperty(name = "url", value = "/images/logo.png")}
        )}
))
@EnableSpringfoxExtension
@SwaggerDefinition(externalDocs = @ExternalDocs(value = "Go to the API list", url = "/api"))
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
