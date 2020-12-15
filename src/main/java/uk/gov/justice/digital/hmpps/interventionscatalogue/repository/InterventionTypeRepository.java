package uk.gov.justice.digital.hmpps.interventionscatalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;

import java.util.UUID;

@Repository
public interface InterventionTypeRepository extends RevisionRepository<InterventionType, UUID, Long>, JpaRepository<InterventionType, UUID> {
}
