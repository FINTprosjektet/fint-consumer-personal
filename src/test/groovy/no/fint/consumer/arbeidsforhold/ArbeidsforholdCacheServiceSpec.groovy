package no.fint.consumer.arbeidsforhold

import no.fint.cache.FintCache
import no.fint.consumer.config.ConsumerProps
import no.fint.consumer.event.ConsumerEventUtil
import no.fint.event.model.Event
import no.fint.model.administrasjon.personal.Arbeidsforhold
import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.relation.FintResource
import spock.lang.Specification

class ArbeidsforholdCacheServiceSpec extends Specification {
    private ArbeidsforholdCacheService cacheService
    private ConsumerEventUtil consumerEventUtil

    void setup() {
        consumerEventUtil = Mock(ConsumerEventUtil)
        cacheService = new ArbeidsforholdCacheService(props: new ConsumerProps(orgs: ['rogfk.no']), consumerEventUtil: consumerEventUtil)
        def cache = new FintCache<FintResource<Arbeidsforhold>>()
        cache.add([new FintResource<Arbeidsforhold>(new Arbeidsforhold(systemId: new Identifikator(identifikatorverdi: '123')))])
        cacheService.put('rogfk.no', cache)
    }

    def "Initialize arbeidsforhold cache for configured organization"() {
        when:
        cacheService.init()
        def cache = cacheService.getCache('rogfk.no')

        then:
        cache != null
    }

    def "Populate cache with all arbeidsforhold"() {
        when:
        cacheService.populateCacheAll()

        then:
        1 * consumerEventUtil.send(_ as Event)
    }

    def "Rebuild cache"() {
        when:
        cacheService.rebuildCache('rogfk.no')
        def values = cacheService.getAll('rogfk.no')

        then:
        1 * consumerEventUtil.send(_ as Event)
        values.size() == 0
    }

    def "Get existing arbeidsforhold"() {
        when:
        def personalressurs = cacheService.getArbeidsforhold('rogfk.no', '123')

        then:
        personalressurs.isPresent()
        personalressurs.get().resource.systemId.identifikatorverdi == '123'
    }

    def "Return empty optional when arbeidsforhold is not found"() {
        when:
        def personalressurs = cacheService.getArbeidsforhold('rogfk.no', '234')

        then:
        !personalressurs.isPresent()
    }
}
