package uk.gov.justice.digital.hmpps.interventionscatalogue.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping
    ProviderDto createProvider(@RequestBody @Valid CreateProvider createProvider) {
        return new ProviderDto(interventionService.createProvider(createProvider));
    }

    @PutMapping(path="{providerId}")
    ProviderDto updateProvider(@RequestBody @Valid UpdateProvider updateProvider) {
        return new ProviderDto(interventionService.updateProvider(updateProvider));
    }
}
