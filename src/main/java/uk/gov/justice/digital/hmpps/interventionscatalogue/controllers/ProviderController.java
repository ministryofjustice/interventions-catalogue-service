package uk.gov.justice.digital.hmpps.interventionscatalogue.controllers;

import org.springframework.data.history.Revision;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.InterventionService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
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

    @GetMapping(path={"{providerId}/versions/{version}", "{providerId}"})
    ProviderDto getProvider(@PathVariable("providerId") UUID providerId, @PathVariable(name ="version", required = false) Optional<Long> version) {
        Optional<Revision<Long, Provider>> provider = version.isPresent() ?
                interventionService.getProvider(providerId, version.get()):
                interventionService.getProvider(providerId);

        if (provider.isPresent()) {
            return new ProviderDto(provider.get().getEntity(), provider.get().getRequiredRevisionNumber());
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
    }

    @GetMapping(path="{providerId}/versions")
    List<Long> getProviderVersions(@PathVariable("providerId") UUID providerId) {
        return interventionService.getProviderVersions(providerId).get()
                .map( e -> e.getRevisionNumber().get())
                .collect(Collectors.toList());
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
