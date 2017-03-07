package no.fint.consumer.personal;

import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.admin.AdminService;
import no.fint.consumer.admin.Health;
import no.fint.consumer.utils.RestEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = RestEndpoints.PERSONAL, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonalController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public Health sendHealth(@RequestHeader(value = "x-org-id", defaultValue = "mock.no") String orgId,
                             @RequestHeader(value = "x-client", defaultValue = "mock") String client) {
        return adminService.healthCheck(orgId, client);
    }
}
