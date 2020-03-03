package uk.gov.justice.digital.hmpps.interventionscatalogue

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InterventionsCatalogueApplication

fun main(args: Array<String>) {
    runApplication<InterventionsCatalogueApplication>(*args)
}
