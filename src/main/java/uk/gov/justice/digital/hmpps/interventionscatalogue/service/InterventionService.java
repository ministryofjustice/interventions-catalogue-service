package uk.gov.justice.digital.hmpps.interventionscatalogue.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderTypeLinkDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.UpdateProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.event.CreateInterventionDataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.BaseEntity;
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

    final ProviderRepository providerRepository;
    final InterventionTypeRepository interventionTypeRepository;
    final InterventionSubTypeRepository interventionSubTypeRepository;
    final SnsService snsService;
    final ApplicationEventPublisher applicationEventPublisher;

    public InterventionService(final ProviderRepository providerRepository,
                               final InterventionTypeRepository interventionTypeRepository,
                               final InterventionSubTypeRepository interventionSubTypeRepository,
                               final SnsService snsService,
                               ApplicationEventPublisher applicationEventPublisher) {
        this.providerRepository = providerRepository;
        this.interventionTypeRepository = interventionTypeRepository;
        this.interventionSubTypeRepository = interventionSubTypeRepository;
        this.snsService = snsService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @TransactionalEventListener
    public void doAfterCommit(final BaseEntity event){
        Revision<Long, Provider> existingProvider = providerRepository.findLastChangeRevision(event.getId()).get();
        event.setVersion(existingProvider.getRevisionNumber().get());
        snsService.sendEvent(event);
    }

    public List<InterventionType> getAllInterventionTypes() {
        return interventionTypeRepository.findAll();
    }

    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    @Transactional
    public CreateProviderResponse createProvider(final CreateProvider createProvider) {
        Provider createdProvider =  providerRepository.save(Provider.builder()
                .name(createProvider.getName())
                .build());

        applicationEventPublisher.publishEvent(createdProvider);

        return new CreateProviderResponse(createdProvider);
    }

    @Transactional
    public Provider updateProvider(final UpdateProvider updateProvider) {
        Optional<Revision<Long, Provider>> existingProvider = providerRepository.findLastChangeRevision(updateProvider.getId());

        if (existingProvider.isPresent()) {
            Provider provider = existingProvider.get().getEntity();
            provider.setName(updateProvider.getName());

            Provider savedProvider = providerRepository.save(provider);

            applicationEventPublisher.publishEvent(savedProvider);

            return savedProvider;
        }

        throw new IllegalArgumentException();
    }

    @CreateInterventionDataEvent
    public void deleteProvider(UUID providerId) {
        providerRepository.deleteById(providerId);
    }

    public Revisions<Long, Provider> getProviderVersions(final UUID providerId) {
        return providerRepository.findRevisions(providerId);
    }

    public Optional<Revision<Long, Provider>> getProvider(final UUID providerId, final Long version) {
        return providerRepository.findRevision(providerId, version);
    }

    public Optional<Revision<Long, Provider>> getProvider(final UUID providerId) {
        return providerRepository.findLastChangeRevision(providerId);
    }

    @CreateInterventionDataEvent
    public InterventionType createInterventionType(final CreateInterventionType createInterventionType) {
        return interventionTypeRepository.save(InterventionType.builder()
                .name(createInterventionType.getName())
                .build());
    }

    @CreateInterventionDataEvent
    public InterventionSubType createInterventionSubType(final CreateInterventionSubType createInterventionSubType) {
        InterventionType interventionType = interventionTypeRepository.getOne(createInterventionSubType.getInterventionTypeId());
        InterventionSubType interventionSubType = InterventionSubType.builder()
                .interventionType(interventionType)
                .name(createInterventionSubType.getName()).build();

        return interventionSubTypeRepository.save(interventionSubType);
    }

    @CreateInterventionDataEvent
    public InterventionType createProviderTypeLink(final CreateProviderTypeLinkDto createProviderTypeLinkDto) {
        Provider provider = providerRepository.findLastChangeRevision(createProviderTypeLinkDto.getProviderId()).get().getEntity();
        InterventionType interventionType = interventionTypeRepository.findLastChangeRevision(createProviderTypeLinkDto.getInterventionTypeId()).get().getEntity();

        System.out.println("id: " + interventionType.getId());
        interventionType.getProviders().add(provider);

        return interventionTypeRepository.save(interventionType);
    }

    public InterventionType getInterventionType(final UUID interventionTypeId) {
        return interventionTypeRepository.findLastChangeRevision(interventionTypeId).get().getEntity();
    }

    public InterventionType deleteProviderTypeLink(final UUID interventionTypeId, final UUID providerId) {
        InterventionType interventionType = interventionTypeRepository.findLastChangeRevision(interventionTypeId).get().getEntity();
        Optional<Provider> provider = interventionType.getProviders().stream().filter(p -> p.getId().equals(providerId)).findAny();
        if (provider.isPresent()) {
            interventionType.getProviders().remove(provider);
            interventionTypeRepository.save(interventionType);
        }
        return interventionType;
    }

    public InterventionType deleteInterventionSubtype(final UUID interventionTypeId, final UUID subtypeId) {
        InterventionType interventionType = interventionTypeRepository.findLastChangeRevision(interventionTypeId).get().getEntity();
        Optional<InterventionSubType> subtype = interventionType.getInterventionSubTypes().stream().filter(p -> p.getId().equals(subtypeId)).findAny();
        if (subtype.isPresent()) {
            interventionType.getInterventionSubTypes().remove(subtype);
            interventionTypeRepository.save(interventionType);
        }
        return interventionType;
    }

    public InterventionType deleteInterventionType(final UUID interventionTypeId) {
        InterventionType interventionType = interventionTypeRepository.findLastChangeRevision(interventionTypeId).get().getEntity();
        interventionTypeRepository.delete(interventionType);
        return null;
    }


}

