package no.fint.consumer.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.hazelcast.config.*;
import lombok.extern.slf4j.Slf4j;
import no.fint.cache.CacheManager;
import no.fint.cache.FintCacheManager;
import no.fint.cache.HazelcastCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class Config {

    @Value("${server.context-path:}")
    private String contextPath;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired(required = false)
    RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        objectMapper.setDateFormat(new ISO8601DateFormat());
        log.info("Before: {}", objectMapper.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS));
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        log.info("Disabled FAIL_ON_EMPTY_BEANS on {}", objectMapper);
        log.info("After: {}", objectMapper.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS));
        if (restTemplate != null) {
            List<AbstractJackson2HttpMessageConverter> converters = restTemplate.getMessageConverters().stream().filter(AbstractJackson2HttpMessageConverter.class::isInstance).map(AbstractJackson2HttpMessageConverter.class::cast).collect(Collectors.toList());
            log.info("Found {} Jackson MessageConverters.", converters.size());
            converters.forEach(converter -> converter.setObjectMapper(objectMapper));
            log.info("Updated ObjectMapper on all Jackson MessageConverters.");
        }
    }

    @Qualifier("linkMapper")
    @Bean
    public Map<String, String> linkMapper() {
        return LinkMapper.linkMapper(contextPath);
    }

}
