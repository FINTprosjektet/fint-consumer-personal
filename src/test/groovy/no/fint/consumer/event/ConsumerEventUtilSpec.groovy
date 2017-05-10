package no.fint.consumer.event

import no.fint.audit.FintAuditService
import no.fint.event.model.Event
import no.fint.events.FintEvents
import no.fint.events.FintEventsHealth
import no.fint.events.HealthCheck
import spock.lang.Specification

class ConsumerEventUtilSpec extends Specification {
    private ConsumerEventUtil consumerEventUtil
    private FintEvents fintEvents
    private FintEventsHealth fintEventsHealth
    private FintAuditService fintAuditService
    private HealthCheck healthCheck

    void setup() {
        healthCheck = Mock(HealthCheck)

        fintEvents = Mock(FintEvents)
        fintAuditService = Mock(FintAuditService)
        fintEventsHealth = Mock(FintEventsHealth) {
            registerClient() >> healthCheck
        }
        consumerEventUtil = new ConsumerEventUtil(fintEvents: fintEvents, fintEventsHealth: fintEventsHealth, fintAuditService: fintAuditService)
    }

    def "Send and receive Event"() {
        given:
        def event = new Event(orgId: 'rogfk.no')

        when:
        consumerEventUtil.init()
        def response = consumerEventUtil.healthCheck(event)

        then:
        3 * fintAuditService.audit(_ as Event)
        1 * healthCheck.check(event) >> event
        response.isPresent()
    }
}
