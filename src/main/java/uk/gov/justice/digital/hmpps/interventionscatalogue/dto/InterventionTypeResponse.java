package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterventionTypeResponse {
    private UUID id;
    private String name;
    private String deliusCode;
    private Boolean active;
    private List<InterventionSubTypeResponse> interventionSubTypes;
}
