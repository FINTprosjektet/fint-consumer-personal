package no.fint;

import com.google.common.collect.Lists;
import no.fint.data.ArbeidsforholdService;
import no.fint.data.PersonService;
import no.fint.data.PersonalressursService;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.events.FintEvents;
import no.fint.events.annotations.FintEventListener;
import no.fint.model.relation.FintResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestListener {

    @Autowired
    private FintEvents fintEvents;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @FintEventListener
    public void recieve(Event<String> event) {
        List<FintResource> resources = Lists.newArrayList();
        switch (event.getAction()) {
            case "GET_ALL_PERSONALRESSURS":
                resources.addAll(personalressursService.getAll());
                break;
            case "GET_ALL_PERSON":
                resources.addAll(personService.getAll());
                break;
            case "GET_ALL_ARBEIDSFORHOLD":
                resources.addAll(arbeidsforholdService.getAll());
                break;
        }

        Event<FintResource> response = new Event<>();
        response.setCorrId(event.getCorrId());
        response.setOrgId(event.getOrgId());
        response.setAction(event.getAction());

        response.setStatus(Status.UPSTREAM_QUEUE);
        response.setTime(System.currentTimeMillis());
        response.setSource("fk");
        response.setClient("vfs");
        response.setData(resources);

        fintEvents.sendUpstream(event.getOrgId(), response);
    }
}
