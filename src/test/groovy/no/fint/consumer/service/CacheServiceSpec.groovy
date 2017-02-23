package no.fint.consumer.service

import no.fint.consumer.test.TestObject
import spock.lang.Specification

class CacheServiceSpec extends Specification {
    private CacheService cacheService

    void setup() {
        cacheService = new CacheService()
        cacheService.init()
    }

    def "Get last updated"() {
        when:
        def lastUpdated = cacheService.getLastUpdated('rogfk.no')

        then:
        lastUpdated != null
    }

    def "Get all persisted objects"() {
        when:
        def objects = cacheService.getAll('rogfk.no')

        then:
        objects.size() == 2
    }

    def "Get all objects with orgId, return empty when no cache is found"() {
        when:
        def objects = cacheService.getAll('unknown-org')

        then:
        objects.size() == 0
    }

    def "Get all objects with orgId, return empty when null orgId"() {
        when:
        def objects = cacheService.getAll(null)

        then:
        objects.size() == 0
    }

    def "Get all objects with orgId and timestamp"() {
        when:
        def objects = cacheService.getAll('rogfk.no', (System.currentTimeMillis() - 5000L))

        then:
        objects.size() == 2
    }

    def "Get all objects with orgId and timestamp, return empty when no cache is found"() {
        when:
        def objects = cacheService.getAll('unknown-org', 0L)

        then:
        objects.size() == 0
    }

    def "Update cache"() {
        when:
        cacheService.update('rogfk.no', [new TestObject('test3'), new TestObject('test4'), new TestObject('test5')])
        def objects = cacheService.getAll('rogfk.no')

        then:
        objects.size() == 3
    }


}
