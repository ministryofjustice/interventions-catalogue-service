package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProvider {
    private UUID id;
    private String name;
}
