package no.fint.consumer.arbeidsforhold

import no.fint.consumer.event.ConsumerEventUtil
import no.fint.consumer.utils.CacheUri
import no.fint.event.model.Event
import spock.lang.Specification

class ArbeidsforholdCacheServiceSpec extends Specification {
    private ArbeidsforholdCacheService cacheService
    private ConsumerEventUtil consumerEventUtil

    void setup() {
        consumerEventUtil = Mock(ConsumerEventUtil)
        cacheService = new ArbeidsforholdCacheService(orgs: ['rogfk.no'], consumerEventUtil: consumerEventUtil)
    }

    def "Initialize arbeidsforhold cache for configured organization"() {
        when:
        cacheService.init()
        def cache = cacheService.getCache(CacheUri.create('rogfk.no', 'arbeidsforhold'))

        then:
        cache != null
    }

    def "Populate cache with all arbeidsforhold"() {
        when:
        cacheService.populateCacheAllArbeidsforhold()

        then:
        1 * consumerEventUtil.send(_ as Event)
    }
}
