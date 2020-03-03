package uk.gov.justice.digital.hmpps.interventionscatalogue.controllers

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Intervention
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionRepository
import javax.validation.Valid

@RestController
@RequestMapping(path = ["intervention"], produces = [MediaType.APPLICATION_JSON_VALUE])
class InterventionsController(private val interventionRepository: InterventionRepository) {
    @GetMapping("/")
    fun getInterventions(): List<Intervention> = interventionRepository.findAll()

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE], path = ["/"])
    fun createIntervention(@RequestBody @Valid createIntervention: Intervention) = interventionRepository.save(createIntervention)
}