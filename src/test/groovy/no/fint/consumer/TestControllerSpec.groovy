package no.fint.consumer

import no.fint.consumer.admin.AdminService
import no.fint.consumer.admin.Health
import no.fint.consumer.test.TestController
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.equalTo
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class TestControllerSpec extends Specification {
    private TestController controller
    private AdminService adminService
    private MockMvc mockMvc

    void setup() {
        adminService = Mock(AdminService)
        controller = new TestController(adminService: adminService, eventModelVersion: '1.0.0')
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    def "Send health check"() {
        when:
        def response = mockMvc.perform(get('/test/health').header('x-org-id', 'rogfk.no').header('x-client', 'test'))

        then:
        1 * adminService.healthCheck('rogfk.no', 'test') >> new Health()
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    def "Get versions"() {
        when:
        def response = mockMvc.perform(get('/test/versions'))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath('$.eventModelVersion').value(equalTo('1.0.0')))
    }

    def "Return bad request when health check without headers"() {
        when:
        def response = mockMvc.perform(get('/test/health'))

        then:
        response.andExpect(status().isBadRequest())
    }


}
