package uk.gov.justice.digital.hmpps.interventionscatalogue.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderTypeLink;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.InterventionSubTypeDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.InterventionTypeDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.InterventionService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "interventiontype", produces = MediaType.APPLICATION_JSON_VALUE)
class InterventionController {
    private InterventionService interventionService;

    public InterventionController(InterventionService interventionService) {
        this.interventionService = interventionService;
    }

    @GetMapping
    List<InterventionTypeDto> getInterventions() {
        return interventionService.getAllInterventionTypes()
                .stream()
                .map(t -> new InterventionTypeDto(t))
                .collect(Collectors.toList());
    }

    @PostMapping
    InterventionTypeDto createIntervention(@RequestBody @Valid CreateInterventionType createInterventionType) {
        return new InterventionTypeDto(interventionService.createInterventionType(createInterventionType));
    }

    @GetMapping(path="{interventionTypeId}/subtype")
    List<InterventionSubTypeDto> getInterventionSubTypes(@PathVariable("interventionTypeId") UUID interventionTypeId) {

        return interventionService.getInterventionType(interventionTypeId)
                .getInterventionSubTypes()
                .stream().map(InterventionSubTypeDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path="{interventionTypeId}/provider")
    List<ProviderDto> getInterventionProviders(@PathVariable("interventionTypeId") UUID interventionTypeId) {

        return interventionService.getInterventionType(interventionTypeId)
                .getProviders()
                .stream().map(ProviderDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping(path="{interventionTypeId}/subtype")
    InterventionSubTypeDto createSubType(@PathVariable("interventionTypeId") UUID interventionTypeId,
                                      @RequestBody @Valid CreateInterventionSubType createInterventionSubType) {

        return new InterventionSubTypeDto(interventionService.createInterventionSubType(createInterventionSubType.withInterventionTypeId(interventionTypeId)));
    }

    @PostMapping(path="{interventionTypeId}/provider")
    InterventionTypeDto linkProviderToType(@PathVariable("interventionTypeId") UUID interventionTypeId,
                                   @RequestBody @Valid CreateProviderTypeLink createProviderTypeLink) {

        return new InterventionTypeDto(interventionService.createProviderTypeLink(createProviderTypeLink.withInterventionTypeId(interventionTypeId)));
    }
}