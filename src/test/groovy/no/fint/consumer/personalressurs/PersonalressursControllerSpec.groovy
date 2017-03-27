package no.fint.consumer.personalressurs

import no.fint.audit.FintAuditService
import no.fint.consumer.utils.CacheUri
import no.fint.consumer.utils.RestEndpoints
import no.fint.event.model.Event
import no.fint.model.administrasjon.personal.Personalressurs
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PersonalressursControllerSpec extends Specification {
    private PersonalressursController controller
    private FintAuditService fintAuditService
    private PersonalressursCacheService cacheService
    private MockMvc mockMvc

    void setup() {
        fintAuditService = Mock(FintAuditService)
        cacheService = Mock(PersonalressursCacheService)
        controller = new PersonalressursController(fintAuditService: fintAuditService, cacheService: cacheService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    def "Get all personalressurser"() {
        when:
        def personalressurser = mockMvc.perform(get(RestEndpoints.PERSONALRESSURS).header('x-org-id', 'rogfk.no').header('x-client', 'vfs'))

        then:
        4 * fintAuditService.audit(_ as Event, _ as Boolean)
        1 * cacheService.getAll(CacheUri.create('rogfk.no', 'personalressurs')) >> [new Personalressurs(), new Personalressurs()]
        personalressurser.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }
}
