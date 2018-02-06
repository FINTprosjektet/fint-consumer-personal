package no.fint.consumer.models.personalressurs

import no.fint.audit.FintAuditService
import no.fint.consumer.utils.RestEndpoints
import no.fint.event.model.HeaderConstants
import no.fint.test.utils.MockMvcSpecification
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc

class PersonalressursControllerSpec extends MockMvcSpecification {
    private PersonalressursController controller
    private PersonalressursCacheService cacheService
    private PersonalressursAssembler assembler
    private MockMvc mockMvc

    void setup() {
        cacheService = Mock(PersonalressursCacheService)
        assembler = Mock(PersonalressursAssembler)
        controller = new PersonalressursController(fintAuditService: Mock(FintAuditService), cacheService: cacheService, assembler: assembler)
        mockMvc = standaloneSetup(controller)
    }

    def "GET last updated"() {
        when:
        def response = mockMvc.perform(get("${RestEndpoints.PERSONALRESSURS}/last-updated")
                .header(HeaderConstants.ORG_ID, 'mock.no'))

        then:
        1 * cacheService.getLastUpdated(_ as String) >> 123L
        response.andExpect(status().isOk())
                .andExpect(jsonPathEquals('$.lastUpdated', '123'))
    }

    def "GET all personalressurser"() {
        when:
        def personalressurser = mockMvc.perform(get(RestEndpoints.PERSONALRESSURS)
                .header(HeaderConstants.ORG_ID, 'rogfk.no')
                .header(HeaderConstants.CLIENT, 'test')
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE))

        then:
        1 * cacheService.getAll('rogfk.no') >> []
        1 * assembler.resources([]) >> ResponseEntity.ok([])
        personalressurser.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }
}
