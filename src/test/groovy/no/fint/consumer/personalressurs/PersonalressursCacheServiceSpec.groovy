package no.fint.consumer.personalressurs

import no.fint.cache.FintCache
import no.fint.consumer.config.ConsumerProps
import no.fint.consumer.event.ConsumerEventUtil
import no.fint.event.model.Event
import no.fint.model.administrasjon.personal.Personalressurs
import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.relation.FintResource
import spock.lang.Specification

class PersonalressursCacheServiceSpec extends Specification {
    private PersonalressursCacheService cacheService
    private ConsumerEventUtil consumerEventUtil

    void setup() {
        consumerEventUtil = Mock(ConsumerEventUtil)
        cacheService = new PersonalressursCacheService(props: new ConsumerProps(orgs: ['rogfk.no']), consumerEventUtil: consumerEventUtil)
        def cache = new FintCache<FintResource<Personalressurs>>()
        cache.add([new FintResource<Personalressurs>(new Personalressurs(ansattnummer: new Identifikator(identifikatorverdi: '123')))])
        cacheService.put('rogfk.no', cache)
    }

    def "Initialize person cache for configured organization"() {
        when:
        cacheService.init()
        def cache = cacheService.getCache('rogfk.no')

        then:
        cache != null
    }

    def "Populate cache with all person"() {
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

    def "Get existing personalressurs"() {
        when:
        def personalressurs = cacheService.getPersonalressurs('rogfk.no', '123')

        then:
        personalressurs.isPresent()
        personalressurs.get().resource.ansattnummer.identifikatorverdi == '123'
    }

    def "Return empty optional when personalressurs is not found"() {
        when:
        def personalressurs = cacheService.getPersonalressurs('rogfk.no', '234')

        then:
        !personalressurs.isPresent()
    }
}
