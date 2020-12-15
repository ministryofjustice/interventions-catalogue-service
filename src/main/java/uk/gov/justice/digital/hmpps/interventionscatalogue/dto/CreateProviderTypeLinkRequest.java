package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class CreateProviderTypeLinkRequest {
    private UUID providerId;
    private UUID interventionTypeId;
}
