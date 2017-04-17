package no.fint.consumer.event

import no.fint.audit.FintAuditService
import no.fint.event.model.Event
import no.fint.events.FintEvents
import spock.lang.Specification

class ConsumerEventUtilSpec extends Specification {
    private ConsumerEventUtil consumerEventUtil
    private FintEvents fintEvents
    private FintAuditService fintAuditService

    void setup() {
        fintEvents = Mock(FintEvents)
        fintAuditService = Mock(FintAuditService)
        consumerEventUtil = new ConsumerEventUtil(fintEvents: fintEvents, fintAuditService: fintAuditService)
    }

    def "Send and receive Event"() {
        given:
        def event = new Event(orgId: 'rogfk.no')

        when:
        def response = consumerEventUtil.sendAndReceive(event)

        then:
        3 * fintAuditService.audit(_ as Event, _ as Boolean)
        1 * fintEvents.sendAndReceiveDownstream('rogfk.no', event, Event) >> Optional.of(event)
        response.isPresent()
    }
}