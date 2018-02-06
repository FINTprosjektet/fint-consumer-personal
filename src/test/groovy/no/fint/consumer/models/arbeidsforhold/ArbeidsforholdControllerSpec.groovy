package no.fint.consumer.models.arbeidsforhold

import no.fint.consumer.utils.RestEndpoints
import no.fint.event.model.HeaderConstants
import no.fint.test.utils.MockMvcSpecification
import org.springframework.test.web.servlet.MockMvc

class ArbeidsforholdControllerSpec extends MockMvcSpecification {
    private ArbeidsforholdController controller
    private ArbeidsforholdCacheService cacheService
    private MockMvc mockMvc

    void setup() {
        cacheService = Mock(ArbeidsforholdCacheService)
        controller = new ArbeidsforholdController(cacheService: cacheService)
        mockMvc = standaloneSetup(controller)
    }

    def "GET last updated"() {
        when:
        def response = mockMvc.perform(get("${RestEndpoints.ARBEIDSFORHOLD}/last-updated")
                .header(HeaderConstants.ORG_ID, 'mock.no'))

        then:
        1 * cacheService.getLastUpdated(_ as String) >> 123L
        response.andExpect(status().isOk())
                .andExpect(jsonPathEquals('$.lastUpdated', '123'))
    }
}