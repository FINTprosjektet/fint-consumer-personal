package no.fint.consumer.admin

import no.fint.cache.CacheService
import no.fint.cache.utils.CacheUri
import no.fint.event.model.Event
import no.fint.events.FintEvents
import no.fint.test.utils.MockMvcSpecification
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class AdminControllerSpec extends MockMvcSpecification {
    private AdminController controller
    private FintEvents fintEvents
    private CacheService cacheService
    private MockMvc mockMvc

    void setup() {
        fintEvents = Mock(FintEvents)
        cacheService = Mock(CacheService)
        controller = new AdminController(fintEvents: fintEvents, cacheServices: [cacheService])
        mockMvc = standaloneSetup(controller)
    }

    def "POST new organisation"() {
        when:
        def response = mockMvc.perform(post('/admin/organisations/123'))

        then:
        1 * fintEvents.sendDownstream(_ as Event)
        response.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, equalTo('http://localhost/admin/organisations/123')))
    }

    def "POST new orgId, return bad request if orgId is already registered"() {
        when:
        def response = mockMvc.perform(post('/admin/organisations/123'))

        then:
        1 * cacheService.getKeys() >> [CacheUri.create('123', 'test')]
        response.andExpect(status().isBadRequest())
    }

    def "GET organisation"() {
        given:
        def cacheUri1 = CacheUri.create('rogfk.no', 'test')
        def cacheUri2 = CacheUri.create('hfk.no', 'test')

        when:
        def response = mockMvc.perform(get('/admin/organisations/rogfk.no'))

        then:
        1 * cacheService.getKeys() >> [cacheUri1, cacheUri2]
        response.andExpect(status().isOk())
                .andExpect(jsonPathEquals('$[0]', cacheUri1))
    }

    def "GET organisations"() {
        given:
        def cacheUri = CacheUri.create('rogfk.no', 'test')

        when:
        def response = mockMvc.perform(get('/admin/organisations'))

        then:
        1 * cacheService.getKeys() >> [cacheUri]
        response.andExpect(status().isOk())
                .andExpect(jsonPathSize('$', 1))
                .andExpect(jsonPathEquals('$[0]', cacheUri))
    }
}
