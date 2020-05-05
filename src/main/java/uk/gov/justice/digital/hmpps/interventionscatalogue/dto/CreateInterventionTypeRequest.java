package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInterventionTypeRequest {
    @NonNull
    private String name;

    @NonNull
    private String deliusCode;

    @NonNull
    private Boolean active;
}
