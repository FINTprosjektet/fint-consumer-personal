package no.fint.consumer.testmode;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(name = "fint.consumer.test-mode", havingValue = "true")
public @interface EnabledIfTestMode {
}
