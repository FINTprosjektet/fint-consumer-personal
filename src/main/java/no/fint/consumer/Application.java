package no.fint.consumer;

import com.github.springfox.loader.EnableSpringfox;
import lombok.extern.slf4j.Slf4j;
import no.fint.audit.EnableFintAudit;
import no.fint.cache.annotations.EnableFintCache;
import no.fint.dependencies.annotations.EnableFintDependencies;
import no.fint.events.annotations.EnableFintEvents;
import no.fint.relations.annotations.EnableFintRelations;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFintDependencies
@EnableFintRelations
@EnableFintEvents
@EnableFintCache
@EnableFintAudit
@EnableScheduling
@EnableSpringfox
@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t,e) -> log.error("Uncaught exception in thread " + t, e));
        SpringApplication.run(Application.class, args);
    }
}
