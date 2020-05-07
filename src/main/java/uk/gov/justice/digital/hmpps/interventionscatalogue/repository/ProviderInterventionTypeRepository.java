package uk.gov.justice.digital.hmpps.interventionscatalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.ProviderInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.ProviderInterventionTypeId;

@Repository
public interface ProviderInterventionTypeRepository extends RevisionRepository<ProviderInterventionType, ProviderInterventionTypeId, Long>, JpaRepository<ProviderInterventionType, ProviderInterventionTypeId> {
}
