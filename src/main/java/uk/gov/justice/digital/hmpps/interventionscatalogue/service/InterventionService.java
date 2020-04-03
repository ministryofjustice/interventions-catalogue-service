package uk.gov.justice.digital.hmpps.interventionscatalogue.service;

import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.CreateProviderTypeLink;
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
    public Provider createProvider(CreateProvider createProvider) {
        return providerRepository.save(Provider.builder()
                .name(createProvider.getName())
                .build());
    }

    public InterventionType createInterventionType(CreateInterventionType createInterventionType) {
        return interventionTypeRepository.save(InterventionType.builder()
                .name(createInterventionType.getName())
                .build());
    }

    public InterventionSubType createInterventionSubType(CreateInterventionSubType createInterventionSubType) {
        InterventionType interventionType = interventionTypeRepository.getOne(createInterventionSubType.getInterventionTypeId());
        InterventionSubType interventionSubType = InterventionSubType.builder()
                .interventionType(interventionType)
                .name(createInterventionSubType.getName()).build();

        return interventionSubTypeRepository.save(interventionSubType);
    }

    public InterventionType createProviderTypeLink(CreateProviderTypeLink createProviderTypeLink) {
        Provider provider = providerRepository.getOne(createProviderTypeLink.getProviderId());
        InterventionType interventionType = interventionTypeRepository.getOne(createProviderTypeLink.getInterventionTypeId());

        System.out.println("id: " + interventionType.getId());
        interventionType.getProviders().add(provider);

        return interventionTypeRepository.save(interventionType);
    }

    public InterventionType getInterventionType(UUID interventionTypeId) {
        return interventionTypeRepository.getOne(interventionTypeId);
    }

    public Provider updateProvider(UpdateProvider updateProvider) {
        Optional<Revision<Integer, Provider>> existingProvider = providerRepository.findLastChangeRevision(updateProvider.getId());

        return null;
    }
}
