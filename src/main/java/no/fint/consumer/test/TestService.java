package no.fint.consumer.test;

import no.fint.audit.FintAuditService;
import no.fint.consumer.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TestService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private FintAuditService fintAuditService;

    public String getLastUpdated(String orgId) {
        long lastUpdated = cacheService.getLastUpdated(orgId);
        Date date = new Date(lastUpdated);
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SS").format(date);
    }

}
