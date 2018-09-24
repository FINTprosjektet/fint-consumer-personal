package no.fint.consumer.models.person

import no.fint.audit.FintAuditService
import no.fint.consumer.config.ConsumerProps
import no.fint.consumer.utils.RestEndpoints
import no.fint.event.model.HeaderConstants
import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.resource.felles.PersonResource
import no.fint.relations.FintResources
import no.fint.test.utils.MockMvcSpecification
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc

class PersonControllerSpec extends MockMvcSpecification {
    private PersonController controller
    private PersonCacheService cacheService
    private PersonLinker linker
    private ConsumerProps props
    private MockMvc mockMvc

    void setup() {
        cacheService = Mock(PersonCacheService)
        linker = Mock(PersonLinker)
        props = Mock(ConsumerProps)
        controller = new PersonController(fintAuditService: Mock(FintAuditService), cacheService: cacheService, linker: linker, props: props)
        mockMvc = standaloneSetup(controller)
    }

    def "GET last updated"() {
        when:
        def response = mockMvc.perform(get("${RestEndpoints.PERSON}/last-updated")
                .header(HeaderConstants.ORG_ID, 'mock.no'))

        then:
        1 * cacheService.getLastUpdated(_ as String) >> 123L
        1 * props.isOverrideOrgId() >> false
        response.andExpect(status().isOk())
                .andExpect(jsonPathEquals('$.lastUpdated', '123'))
    }

    def "GET all personer"() {
        when:
        def response = mockMvc.perform(get(RestEndpoints.PERSON)
                .header(HeaderConstants.ORG_ID, 'rogfk.no')
                .header(HeaderConstants.CLIENT, 'test')
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE))

        then:
        1 * props.isOverrideOrgId() >> false
        1 * cacheService.getAll('rogfk.no') >> []
        1 * linker.toResources([]) >> new FintResources<PersonResource>()
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    def "GET person by fodselsnummer ids"() {
        given:
        def resource1 = new PersonResource(fodselsnummer: new Identifikator(identifikatorverdi: '123'))
        def resource2 = new PersonResource(fodselsnummer: new Identifikator(identifikatorverdi: '234'))
        def resource3 = new PersonResource(fodselsnummer: new Identifikator(identifikatorverdi: '345'))

        when:
        def response = mockMvc.perform(post("${RestEndpoints.PERSON}/fodselsnummer")
                .header(HeaderConstants.ORG_ID, 'rogfk.no')
                .header(HeaderConstants.CLIENT, 'test')
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content('["123", "234"]'))

        then:
        1 * cacheService.getAll('rogfk.no') >> [resource1, resource2, resource3]
        1 * linker.toResources(_) >> new FintResources<PersonResource>([resource1, resource2], 'http://localhost')
        response.andExpect(status().isOk())
                .andExpect(jsonPathSize('$._embedded._entries', 2))
    }

}
