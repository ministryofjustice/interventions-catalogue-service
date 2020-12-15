package uk.gov.justice.digital.hmpps.interventionscatalogue.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionSubTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderTypeLinkRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.InterventionSubTypeResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.InterventionTypeResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderTypeLinkResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateInterventionSubTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateInterventionTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.mappers.ProviderMapper;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.InterventionService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
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
    List<InterventionTypeResponse> getInterventions() {
        return ProviderMapper.INSTANCE.map(interventionService.getAllInterventionTypes());
    }

    @GetMapping(path="{interventionTypeId}")
    InterventionTypeResponse getIntervention(@PathVariable("interventionTypeId") UUID interventionTypeId) {
        return ProviderMapper.INSTANCE.map(interventionService.getInterventionType(interventionTypeId));
    }

    @PostMapping
    InterventionTypeResponse createIntervention(@RequestBody @Valid CreateInterventionTypeRequest createInterventionTypeRequest) {
        return ProviderMapper.INSTANCE.map(interventionService.createInterventionType(createInterventionTypeRequest).getEntity());
    }

    @PutMapping(path="{interventionTypeId}")
    InterventionTypeResponse updateIntervention(@PathVariable("interventionTypeId") UUID interventionTypeId, @RequestBody @Valid UpdateInterventionTypeRequest updateInterventionTypeRequest) {
        updateInterventionTypeRequest.setId(interventionTypeId);
        return ProviderMapper.INSTANCE.map(interventionService.updateInterventionType(updateInterventionTypeRequest).getEntity());
    }

    @DeleteMapping(path="{interventionTypeId}")
    void deleteType(@PathVariable("interventionTypeId") UUID interventionTypeId) {
        interventionService.deleteInterventionType(interventionTypeId);
    }

    @GetMapping(path="{interventionTypeId}/subtype")
    List<InterventionSubTypeResponse> getInterventionSubTypes(@PathVariable("interventionTypeId") UUID interventionTypeId) {
        return ProviderMapper.INSTANCE.map(interventionService.getInterventionType(interventionTypeId).getInterventionSubTypes());
    }

    @PostMapping(path="{interventionTypeId}/subtype")
    InterventionSubTypeResponse createSubType(@PathVariable("interventionTypeId") UUID interventionTypeId,
                                              @RequestBody @Valid CreateInterventionSubTypeRequest createInterventionSubTypeRequest) {

        return ProviderMapper.INSTANCE.map(interventionService.createInterventionSubType(createInterventionSubTypeRequest
                .withInterventionTypeId(interventionTypeId))
                .getEntity());
    }

    @PutMapping(path="{interventionTypeId}/subtype/{subtypeId}")
    InterventionSubTypeResponse updateSubType(@PathVariable("interventionTypeId") UUID interventionTypeId,
                                              @PathVariable("subtypeId") UUID subtypeId,
                                              @RequestBody @Valid UpdateInterventionSubTypeRequest updateInterventionSubTypeRequest) {

        updateInterventionSubTypeRequest.setInterventionTypeId(interventionTypeId);
        updateInterventionSubTypeRequest.setId(subtypeId);
        return ProviderMapper.INSTANCE.map(interventionService.updateInterventionSubType(updateInterventionSubTypeRequest)
                .getEntity());
    }


    @DeleteMapping(path="{interventionTypeId}/subtype/{subtypeId}")
    InterventionSubTypeResponse deleteSubtypeFromType(@PathVariable("interventionTypeId") UUID interventionTypeId,
                                                   @PathVariable("subtypeId") UUID subtypeId) {

        return ProviderMapper.INSTANCE.map(interventionService.deleteInterventionSubtype(interventionTypeId, subtypeId).getEntity());
    }

    @GetMapping(path="{interventionTypeId}/provider")
    Set<ProviderDto> getInterventionProviders(@PathVariable("interventionTypeId") UUID interventionTypeId) {
        var providers = interventionService.getInterventionType(interventionTypeId)
                .getProviderInterventionTypes()
                .stream()
                .map(pit -> pit.getProvider())
                .collect(Collectors.toList());
        return ProviderMapper.INSTANCE.map(providers);
    }

    @PostMapping(path="{interventionTypeId}/provider")
    ProviderTypeLinkResponse linkProviderToType(@PathVariable("interventionTypeId") UUID interventionTypeId,
                                                @RequestBody @Valid CreateProviderTypeLinkRequest createProviderTypeLinkRequest) {

        return ProviderMapper.INSTANCE.map(interventionService.createProviderTypeLink(createProviderTypeLinkRequest
                .withInterventionTypeId(interventionTypeId))
                .getEntity());
    }

    @DeleteMapping(path="{interventionTypeId}/provider/{providerId}")
    ProviderTypeLinkResponse deleteProviderFromType(@PathVariable("interventionTypeId") UUID interventionTypeId,
                                                    @PathVariable("providerId") UUID providerId) {

        return ProviderMapper.INSTANCE.map(interventionService.deleteProviderTypeLink(interventionTypeId, providerId).getEntity());
    }
}
