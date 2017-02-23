package no.fint.consumer.test

import no.fint.audit.FintAuditService
import no.fint.consumer.service.CacheService
import spock.lang.Specification

class TestServiceSpec extends Specification {
    private TestService service
    private CacheService cacheService
    private FintAuditService fintAuditService

    void setup() {
        cacheService = Mock(CacheService)
        fintAuditService = Mock(FintAuditService)
        service = new TestService(cacheService: cacheService, fintAuditService: fintAuditService)
    }

    def "Get last updated"() {
        when:
        def lastUpdated = service.getLastUpdated('rogfk.no')

        then:
        1 * cacheService.getLastUpdated('rogfk.no') >> System.currentTimeMillis()
        lastUpdated != null
    }
}
