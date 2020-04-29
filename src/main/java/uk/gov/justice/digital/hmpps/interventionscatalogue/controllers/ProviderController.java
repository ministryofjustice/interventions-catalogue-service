package uk.gov.justice.digital.hmpps.interventionscatalogue.controllers;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.history.Revision;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ErrorDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.RevisionDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateProviderRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.mappers.ProviderMapper;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.InterventionService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(path = "provider", produces = MediaType.APPLICATION_JSON_VALUE)
class ProviderController {
    private InterventionService interventionService;

    public ProviderController(InterventionService interventionService) {
        this.interventionService = interventionService;
    }

    @ExceptionHandler({ EntityNotFoundException.class, EmptyResultDataAccessException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleException(EntityNotFoundException exception) {
        return new ErrorDto("Entity not found");
    }

    @GetMapping
    Set<ProviderDto> getProviders() {
        return ProviderMapper.INSTANCE.map(interventionService.getAllProviders());
    }

    @GetMapping(path={"{providerId}"})
    ProviderDto getProvider(@PathVariable("providerId") UUID providerId) {
        return ProviderMapper.INSTANCE.map(interventionService.getProvider(providerId));
    }

    @GetMapping(path={"{providerId}/versions/{version}"})
    ProviderDto getProvider(@PathVariable("providerId") UUID providerId, @PathVariable(name ="version", required = false) Optional<Long> version) {
        Optional<Revision<Long, Provider>> provider = interventionService.getProvider(providerId, version.get());

        if (provider.isPresent()) {
            return ProviderMapper.INSTANCE.map(provider.get().getEntity());
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
    }

    @GetMapping(path="{providerId}/versions")
    List<RevisionDto> getProviderVersions(@PathVariable("providerId") UUID providerId) {
        return ProviderMapper.INSTANCE.map(interventionService.getProviderVersions(providerId).get());
    }

    @PostMapping
    ProviderDto createProvider(@RequestBody @Valid CreateProviderRequest createProviderRequest) {
        return ProviderMapper.INSTANCE.map(interventionService.createProvider(createProviderRequest).getEntity());
    }

    @PutMapping(path="{providerId}")
    ProviderDto updateProvider(@PathVariable("providerId") UUID providerId, @RequestBody @Valid UpdateProviderRequest updateProvider) {
        updateProvider.setId(providerId);
        return ProviderMapper.INSTANCE.map(interventionService.updateProvider(updateProvider).getEntity());
    }

    @DeleteMapping(path="{providerId}")
    ProviderDto deleteProvider(@PathVariable("providerId") UUID providerId) {
        return ProviderMapper.INSTANCE.map(interventionService.deleteProvider(providerId).getEntity());
    }
}
