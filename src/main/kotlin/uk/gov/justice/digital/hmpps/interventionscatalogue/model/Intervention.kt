package uk.gov.justice.digital.hmpps.interventionscatalogue.model

import com.fasterxml.jackson.annotation.JsonInclude
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Intervention (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String
)