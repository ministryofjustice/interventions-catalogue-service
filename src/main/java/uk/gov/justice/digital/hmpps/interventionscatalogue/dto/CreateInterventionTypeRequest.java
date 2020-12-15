package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CreateInterventionTypeRequest {
    @NotNull
    private String name;

    @NotNull
    private String deliusCode;

    @NotNull
    private Boolean active;
}
