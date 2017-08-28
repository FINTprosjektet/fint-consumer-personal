package no.fint.consumer.person

import no.fint.cache.FintCache
import no.fint.consumer.config.ConsumerProps
import no.fint.consumer.event.ConsumerEventUtil
import no.fint.event.model.Event
import no.fint.model.felles.Person
import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.relation.FintResource
import spock.lang.Specification

class PersonCacheServiceSpec extends Specification {
    private PersonCacheService cacheService
    private ConsumerEventUtil consumerEventUtil

    void setup() {
        consumerEventUtil = Mock(ConsumerEventUtil)
        cacheService = new PersonCacheService(props: new ConsumerProps(orgs: ['rogfk.no']), consumerEventUtil: consumerEventUtil)
        def cache = new FintCache<FintResource<Person>>()
        cache.add([new FintResource<Person>(new Person(fodselsnummer: new Identifikator(identifikatorverdi: '123')))])
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
        given:


        when:
        cacheService.rebuildCache('rogfk.no')
        def values = cacheService.getAll('rogfk.no')

        then:
        1 * consumerEventUtil.send(_ as Event)
        values.size() == 0
    }

    def "Get existing person"() {
        when:
        def personalressurs = cacheService.getPerson('rogfk.no', '123')

        then:
        personalressurs.isPresent()
        personalressurs.get().resource.fodselsnummer.identifikatorverdi == '123'
    }

    def "Return empty optional when person is not found"() {
        when:
        def personalressurs = cacheService.getPerson('rogfk.no', '234')

        then:
        !personalressurs.isPresent()
    }
}
