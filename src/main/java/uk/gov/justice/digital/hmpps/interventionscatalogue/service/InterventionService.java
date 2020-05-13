package uk.gov.justice.digital.hmpps.interventionscatalogue.service;

import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionSubTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderTypeLinkRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEventType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderTypeLinkResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateInterventionSubTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateInterventionTypeRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateProviderRequest;
import uk.gov.justice.digital.hmpps.interventionscatalogue.event.CreateInterventionDataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.ProviderInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.ProviderInterventionTypeId;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionSubTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.ProviderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InterventionService {
    final ProviderRepository providerRepository;
    final InterventionTypeRepository interventionTypeRepository;
    final InterventionSubTypeRepository interventionSubTypeRepository;
    final SnsService snsService;

    public InterventionService(final ProviderRepository providerRepository,
                               final InterventionTypeRepository interventionTypeRepository,
                               final InterventionSubTypeRepository interventionSubTypeRepository,
                               final SnsService snsService) {
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

    @Transactional
    @CreateInterventionDataEvent
    public DataEvent<Provider> createProvider(final CreateProviderRequest createProviderRequest) {
        Provider createdProvider =  providerRepository.save(Provider.builder()
                .name(createProviderRequest.getName())
                .deliusCode(createProviderRequest.getDeliusCode())
                .active(createProviderRequest.getActive())
                .build());

        return new DataEvent<>(createdProvider, DataEventType.CREATED);
    }

    @Transactional
    @CreateInterventionDataEvent
    public DataEvent<Provider> updateProvider(final UpdateProviderRequest updateProvider) {
        var existingProvider = providerRepository.getOne(updateProvider.getId());

        existingProvider.setName(updateProvider.getName());
        existingProvider.setDeliusCode(updateProvider.getDeliusCode());
        existingProvider.setActive(updateProvider.getActive());

        return new DataEvent<>(existingProvider, DataEventType.UPDATED);
    }

    @Transactional
    @CreateInterventionDataEvent
    public DataEvent<Provider> deleteProvider(UUID providerId) {
        var provider = providerRepository.getOne(providerId);
        return new DataEvent<>(Provider.builder()
                .id(providerId)
                .deliusCode(provider.getDeliusCode())
                .build(), DataEventType.DELETED);
    }

    public Revisions<Long, Provider> getProviderVersions(final UUID providerId) {
        return providerRepository.findRevisions(providerId);
    }

    public Optional<Revision<Long, Provider>> getProvider(final UUID providerId, final Long version) {
        return providerRepository.findRevision(providerId, version);
    }

    public Provider getProvider(final UUID providerId) {
        return providerRepository.getOne(providerId);
    }

    @Transactional
    @CreateInterventionDataEvent
    public DataEvent<InterventionType> createInterventionType(final CreateInterventionTypeRequest createInterventionTypeRequest) {
        return new DataEvent<>(interventionTypeRepository.save(InterventionType.builder()
                .name(createInterventionTypeRequest.getName())
                .deliusCode(createInterventionTypeRequest.getDeliusCode())
                .active(createInterventionTypeRequest.getActive())
                .build()), DataEventType.CREATED);
    }

    @Transactional
    @CreateInterventionDataEvent
    public DataEvent<InterventionType> updateInterventionType(final UpdateInterventionTypeRequest updateInterventionTypeRequest) {
        var existingIntervention = interventionTypeRepository.getOne(updateInterventionTypeRequest.getId());
        existingIntervention.setActive(updateInterventionTypeRequest.getActive());
        existingIntervention.setDeliusCode(updateInterventionTypeRequest.getDeliusCode());
        existingIntervention.setName(updateInterventionTypeRequest.getName());

        return new DataEvent<>(interventionTypeRepository.save(existingIntervention), DataEventType.UPDATED);
    }
    @Transactional
    @CreateInterventionDataEvent
    public DataEvent<InterventionSubType> createInterventionSubType(final CreateInterventionSubTypeRequest createInterventionSubTypeRequest) {
        InterventionType interventionType = interventionTypeRepository
                .getOne(createInterventionSubTypeRequest.getInterventionTypeId());

        InterventionSubType interventionSubType = InterventionSubType.builder()
                .interventionType(interventionType)
                .name(createInterventionSubTypeRequest.getName())
                .deliusCode(createInterventionSubTypeRequest.getDeliusCode())
                .active(createInterventionSubTypeRequest.getActive())
                .build();

        return new DataEvent<>(interventionSubTypeRepository.save(interventionSubType), DataEventType.CREATED);
    }

    @Transactional
    @CreateInterventionDataEvent
    public DataEvent<InterventionSubType> updateInterventionSubType(final UpdateInterventionSubTypeRequest updateInterventionSubTypeRequest) {
        InterventionType interventionType = interventionTypeRepository
                .getOne(updateInterventionSubTypeRequest.getInterventionTypeId());

        var interventionSubType = interventionType.getInterventionSubTypes()
                .stream()
                .filter(ist -> ist.getId().equals(updateInterventionSubTypeRequest.getId()))
                .findFirst();

        if (interventionSubType.isPresent()) {
            var subType = interventionSubType.get();
            subType.setActive(updateInterventionSubTypeRequest.getActive());
            subType.setName(updateInterventionSubTypeRequest.getName());
            subType.setDeliusCode(updateInterventionSubTypeRequest.getDeliusCode());

            return new DataEvent<>(interventionSubTypeRepository.save(subType), DataEventType.UPDATED);
        }
        throw new RuntimeException("Couldn't find intervention sub type to update");
    }

    @Transactional
    @CreateInterventionDataEvent
    public DataEvent<ProviderInterventionType> createProviderTypeLink(final CreateProviderTypeLinkRequest createProviderTypeLinkRequest) {
        Provider provider = providerRepository.getOne(createProviderTypeLinkRequest.getProviderId());

        InterventionType interventionType = interventionTypeRepository.getOne(createProviderTypeLinkRequest.getInterventionTypeId());

        var providerInterventionType = ProviderInterventionType.builder()
                .id(new ProviderInterventionTypeId(provider.getId(), interventionType.getId()))
                .provider(provider)
                .interventionType(interventionType).build();

        interventionType.getProviderInterventionTypes().add(providerInterventionType);

        interventionTypeRepository.save(interventionType);

        return new DataEvent<>(providerInterventionType, DataEventType.CREATED);
    }

    public InterventionType getInterventionType(final UUID interventionTypeId) {
        return interventionTypeRepository.getOne(interventionTypeId);
    }

    @Transactional
    @CreateInterventionDataEvent
    public DataEvent<ProviderInterventionType> deleteProviderTypeLink(final UUID interventionTypeId, final UUID providerId) {
        InterventionType interventionType = interventionTypeRepository.getOne(interventionTypeId);

        Optional<ProviderInterventionType> providerInterventionType = interventionType
                .getProviderInterventionTypes()
                .stream()
                .filter(pit -> pit.getProvider().getId().equals(providerId))
                .findAny();

        if (providerInterventionType.isPresent()) {
            interventionType.getProviderInterventionTypes().remove(providerInterventionType.get());
            interventionTypeRepository.save(interventionType);

            return new DataEvent<>(providerInterventionType.get(), DataEventType.DELETED);
        }
        throw new IllegalArgumentException("Link does not exist to be deleted");
    }

    @Transactional
    @CreateInterventionDataEvent
    public DataEvent<InterventionSubType> deleteInterventionSubtype(final UUID interventionTypeId, final UUID subtypeId) {
        InterventionType interventionType = interventionTypeRepository.findLastChangeRevision(interventionTypeId).get().getEntity();
        Optional<InterventionSubType> subtype = interventionType.getInterventionSubTypes()
                .stream()
                .filter(p -> p.getId().equals(subtypeId)).findAny();
        if (subtype.isPresent()) {
            interventionType.getInterventionSubTypes().remove(subtype.get());
            interventionTypeRepository.save(interventionType);
            return new DataEvent<>(subtype.get(), DataEventType.DELETED);
        }
        throw new IllegalArgumentException();
    }

    @Transactional
    @CreateInterventionDataEvent
    public DataEvent<InterventionType> deleteInterventionType(final UUID interventionTypeId) {
        InterventionType interventionType = interventionTypeRepository.findLastChangeRevision(interventionTypeId).get().getEntity();
        interventionTypeRepository.delete(interventionType);
        return new DataEvent<>(InterventionType.builder()
                .id(interventionTypeId)
                .deliusCode(interventionType.getDeliusCode())
                .createdDate(interventionType.getCreatedDate()).build(), DataEventType.DELETED);
    }
}

