package uk.gov.justice.digital.hmpps.interventionscatalogue.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Intervention;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionRepository;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "intervention", produces = MediaType.APPLICATION_JSON_VALUE)
class InterventionsController {
    private InterventionRepository interventionRepository;

    public InterventionsController(InterventionRepository interventionRepository) {
        this.interventionRepository = interventionRepository;
    }

    @GetMapping
    List<Intervention> getInterventions() {
        return interventionRepository.findAll();
    }

    @PostMapping
    Intervention createIntervention(@RequestBody @Valid Intervention createIntervention) {
        return interventionRepository.save(createIntervention);
    }
}