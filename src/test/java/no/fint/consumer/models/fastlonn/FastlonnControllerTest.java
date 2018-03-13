package no.fint.consumer.models.fastlonn;

import no.fint.consumer.utils.RestEndpoints;
import no.fint.model.administrasjon.kompleksedatatyper.Beskjeftigelse;
import no.fint.model.administrasjon.kompleksedatatyper.Fasttillegg;
import no.fint.model.administrasjon.kompleksedatatyper.Kontostreng;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.administrasjon.personal.Fastlonn;
import no.fint.model.administrasjon.personal.Personalressurs;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FastlonnControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void postFastlonn() {
        Fastlonn fastlonn = new Fastlonn();
        Beskjeftigelse beskjeftigelse = new Beskjeftigelse();
        beskjeftigelse.setProsent(10000L);
        //beskjeftigelse.setKontostreng(new Kontostreng());
        Fasttillegg fasttillegg = new Fasttillegg();
        fasttillegg.setBelop(100000L);
        //fasttillegg.setKontostreng(new Kontostreng());
        fastlonn.setBeskjeftigelse(Collections.singletonList(beskjeftigelse));
        fastlonn.setFasttillegg(Collections.singletonList(fasttillegg));
        fastlonn.setAttestert(new Date());
        fastlonn.setAnvist(new Date());
        Periode periode = new Periode();
        periode.setStart(new Date());
        periode.setSlutt(new Date());
        fastlonn.setPeriode(periode);

        FintResource resource = FintResource.with(fastlonn)
            .addRelations(new Relation.Builder().with(Fastlonn.Relasjonsnavn.ARBEIDSFORHOLD).forType(Arbeidsforhold.class).field("systemid").value("1234").build())
            .addRelations(new Relation.Builder().with(Fastlonn.Relasjonsnavn.ATTESTANT).forType(Personalressurs.class).field("ansattnummer").value("100000").build())
            .addRelations(new Relation.Builder().with(Fastlonn.Relasjonsnavn.ANVISER).forType(Personalressurs.class).field("ansattnummer").value("100001").build());

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-org-id", "pwf.no");
        headers.add("x-client", "test");

        ResponseEntity<String> result = restTemplate.exchange("http://localhost:{port}{endpoint}", HttpMethod.POST, new HttpEntity<>(resource, headers), String.class, port, RestEndpoints.FASTLONN);
        System.out.println("result.getBody() = " + result.getBody());
        Assert.assertThat(result.getStatusCode().is2xxSuccessful(), Matchers.is(true));
    }

    @Ignore
    @Test
    public void postFastlonnJson() throws IOException {
        String content = IOUtils.toString(new ClassPathResource("fastlonn.json").getInputStream(), "UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-org-id", "pwf.no");
        headers.add("x-client", "test");
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<String> result = restTemplate.postForEntity("http://localhost:{port}{endpoint}", new HttpEntity<>(content, headers), String.class, port, RestEndpoints.FASTLONN);
        System.out.println("result = " + result);
        Assert.assertThat(result.getStatusCode().is2xxSuccessful(), Matchers.is(true));
    }
}
