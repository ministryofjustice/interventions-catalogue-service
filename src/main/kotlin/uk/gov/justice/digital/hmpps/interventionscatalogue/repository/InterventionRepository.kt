package uk.gov.justice.digital.hmpps.interventionscatalogue.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Intervention

@Repository
interface InterventionRepository : JpaRepository<Intervention, Long> {
}