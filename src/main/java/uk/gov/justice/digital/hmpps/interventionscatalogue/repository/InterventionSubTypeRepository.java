package uk.gov.justice.digital.hmpps.interventionscatalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionSubType;

import java.util.UUID;

@Repository
public interface InterventionSubTypeRepository extends RevisionRepository<InterventionSubType, UUID, Long>, JpaRepository<InterventionSubType, UUID> {
}
