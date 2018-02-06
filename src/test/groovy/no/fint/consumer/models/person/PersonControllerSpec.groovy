package no.fint.consumer.models.person

import no.fint.audit.FintAuditService
import no.fint.consumer.config.ConsumerProps
import no.fint.consumer.utils.RestEndpoints
import no.fint.event.model.HeaderConstants
import no.fint.test.utils.MockMvcSpecification
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc

class PersonControllerSpec extends MockMvcSpecification {
    private PersonController controller
    private PersonCacheService cacheService
    private PersonAssembler assembler
    private ConsumerProps props
    private MockMvc mockMvc

    void setup() {
        cacheService = Mock(PersonCacheService)
        assembler = Mock(PersonAssembler)
        props = Mock(ConsumerProps)
        controller = new PersonController(fintAuditService: Mock(FintAuditService), cacheService: cacheService, assembler: assembler, props: props)
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
        1 * assembler.resources([]) >> ResponseEntity.ok([])
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

}
