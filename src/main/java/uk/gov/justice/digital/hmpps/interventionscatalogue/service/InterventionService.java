package uk.gov.justice.digital.hmpps.interventionscatalogue.service;

import lombok.NonNull;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderTypeLinkDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.event.CreateInterventionDataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionSubTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.ProviderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class InterventionService {

    ProviderRepository providerRepository;
    InterventionTypeRepository interventionTypeRepository;
    InterventionSubTypeRepository interventionSubTypeRepository;
    SnsService snsService;

    public InterventionService(ProviderRepository providerRepository,
                               InterventionTypeRepository interventionTypeRepository,
                               InterventionSubTypeRepository interventionSubTypeRepository,
                               SnsService snsService) {
        this.providerRepository = providerRepository;
        this.interventionTypeRepository = interventionTypeRepository;
        this.interventionSubTypeRepository = interventionSubTypeRepository;
        this.snsService = snsService;
    }

    public List<InterventionType> getAllInterventionTypes() {
        return interventionTypeRepository.findAll();
    }

    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    @CreateInterventionDataEvent
    public CreateProviderResponse createProvider(CreateProvider createProvider) {
        Provider createdProvider =  providerRepository.save(Provider.builder()
                .name(createProvider.getName())
                .build());

        return new CreateProviderResponse(createdProvider);
    }

    @CreateInterventionDataEvent
    public Provider updateProvider(UpdateProvider updateProvider) {
        Optional<Revision<Integer, Provider>> existingProvider = providerRepository.findLastChangeRevision(updateProvider.getId());

        if (existingProvider.isPresent()) {
            Provider provider = existingProvider.get().getEntity();
            provider.setName(updateProvider.getName());
            return providerRepository.save(provider);
        }

        throw new IllegalArgumentException();
    }

    @CreateInterventionDataEvent
    public void deleteProvider(UUID providerId) {
        providerRepository.deleteById(providerId);
    }

    @CreateInterventionDataEvent
    public InterventionType createInterventionType(CreateInterventionType createInterventionType) {
        return interventionTypeRepository.save(InterventionType.builder()
                .name(createInterventionType.getName())
                .build());
    }

    @CreateInterventionDataEvent
    public InterventionSubType createInterventionSubType(CreateInterventionSubType createInterventionSubType) {
        InterventionType interventionType = interventionTypeRepository.getOne(createInterventionSubType.getInterventionTypeId());
        InterventionSubType interventionSubType = InterventionSubType.builder()
                .interventionType(interventionType)
                .name(createInterventionSubType.getName()).build();

        return interventionSubTypeRepository.save(interventionSubType);
    }

    @CreateInterventionDataEvent
    public InterventionType createProviderTypeLink(CreateProviderTypeLinkDto createProviderTypeLinkDto) {
        Provider provider = providerRepository.findLastChangeRevision(createProviderTypeLinkDto.getProviderId()).get().getEntity();
        InterventionType interventionType = interventionTypeRepository.findLastChangeRevision(createProviderTypeLinkDto.getInterventionTypeId()).get().getEntity();

        System.out.println("id: " + interventionType.getId());
        interventionType.getProviders().add(provider);

        return interventionTypeRepository.save(interventionType);
    }

    public InterventionType getInterventionType(UUID interventionTypeId) {
        return interventionTypeRepository.findLastChangeRevision(interventionTypeId).get().getEntity();
    }

    public InterventionType deleteProviderTypeLink(UUID interventionTypeId, UUID providerId) {
        InterventionType interventionType = interventionTypeRepository.findLastChangeRevision(interventionTypeId).get().getEntity();
        Optional<Provider> provider = interventionType.getProviders().stream().filter(p -> p.getId().equals(providerId)).findAny();
        if (provider.isPresent()) {
            interventionType.getProviders().remove(provider);
            interventionTypeRepository.save(interventionType);
        }
        return interventionType;
    }

    public InterventionType deleteInterventionSubtype(UUID interventionTypeId, UUID subtypeId) {
        InterventionType interventionType = interventionTypeRepository.findLastChangeRevision(interventionTypeId).get().getEntity();
        Optional<InterventionSubType> subtype = interventionType.getInterventionSubTypes().stream().filter(p -> p.getId().equals(subtypeId)).findAny();
        if (subtype.isPresent()) {
            interventionType.getInterventionSubTypes().remove(subtype);
            interventionTypeRepository.save(interventionType);
        }
        return interventionType;
    }

    public InterventionType deleteInterventionType(UUID interventionTypeId) {
        InterventionType interventionType = interventionTypeRepository.findLastChangeRevision(interventionTypeId).get().getEntity();
        interventionTypeRepository.delete(interventionType);
        return null;
    }

    public Provider getProvider(UUID providerId) {
        return providerRepository.findLastChangeRevision(providerId).get().getEntity();
    }
}

