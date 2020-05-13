package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@With
public class CreateInterventionSubTypeRequest {
    private UUID interventionTypeId;

    @NotNull
    private String name;

    @NotNull
    private String deliusCode;

    @NotNull
    private Boolean active;
}
