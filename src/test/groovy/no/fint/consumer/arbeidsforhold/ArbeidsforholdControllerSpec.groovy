package no.fint.consumer.arbeidsforhold

import com.github.spock.spring.utils.MockMvcSpecification
import no.fint.consumer.utils.RestEndpoints
import org.hamcrest.CoreMatchers
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

    def "Get last updated"() {
        when:
        def response = mockMvc.perform(get("${RestEndpoints.ARBEIDSFORHOLD}/last-updated")
                .header('x-org-id', 'mock.no'))

        then:
        1 * cacheService.getLastUpdated(_ as String) >> 123L
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.lastUpdated').value(CoreMatchers.equalTo('123')))
    }
}
