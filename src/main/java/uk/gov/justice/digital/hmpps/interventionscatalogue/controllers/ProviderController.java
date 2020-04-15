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
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.InterventionService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "provider", produces = MediaType.APPLICATION_JSON_VALUE)
class ProviderController {
    private InterventionService interventionService;

    public ProviderController(InterventionService interventionService) {
        this.interventionService = interventionService;
    }

    @GetMapping
    List<ProviderDto> getProviders() {
        return interventionService.getAllProviders().stream().map(ProviderDto::new).collect(Collectors.toList());
    }

    @GetMapping(path="{providerId}")
    ProviderDto getProvider(@PathVariable("providerId") UUID providerId) {
        return new ProviderDto(interventionService.getProvider(providerId));
    }

    @PostMapping
    ProviderDto createProvider(@RequestBody @Valid CreateProvider createProvider) {
        return new ProviderDto(interventionService.createProvider(createProvider));
    }

    @PutMapping(path="{providerId}")
    ProviderDto updateProvider(@PathVariable("providerId") UUID providerId, @RequestBody @Valid UpdateProvider updateProvider) {
        updateProvider.setId(providerId);
        return new ProviderDto(interventionService.updateProvider(updateProvider));
    }

    @DeleteMapping(path="{providerId}")
    void deleteProvider(@PathVariable("providerId") UUID providerId) {
        interventionService.deleteProvider(providerId);
    }
}
