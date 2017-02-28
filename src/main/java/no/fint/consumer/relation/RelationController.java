package no.fint.consumer.relation;

import lombok.extern.slf4j.Slf4j;
import no.fint.consumer.utils.CacheUri;
import no.fint.consumer.utils.RestEndpoints;
import no.fint.relation.model.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = RestEndpoints.RELATION, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RelationController {

    @Autowired
    private RelationCacheService relationCacheService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Relation> getRelations(@RequestHeader("x-org-id") String orgId) {
        return relationCacheService.getAll(CacheUri.create(orgId, "relations"));
    }

}
