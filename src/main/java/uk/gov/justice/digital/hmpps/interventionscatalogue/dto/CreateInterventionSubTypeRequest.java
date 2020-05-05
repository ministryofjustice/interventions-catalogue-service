package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class CreateInterventionSubTypeRequest {
    private UUID interventionTypeId;

    @NonNull
    private String name;

    @NonNull
    private String deliusCode;

    @NonNull
    private Boolean active;
}
