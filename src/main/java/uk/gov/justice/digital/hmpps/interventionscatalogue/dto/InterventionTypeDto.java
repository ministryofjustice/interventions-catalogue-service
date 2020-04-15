package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;

import javax.persistence.Entity;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterventionTypeDto {
    private UUID id;
    private String name;
    private List<InterventionSubTypeDto> interventionSubTypes;

    public InterventionTypeDto(InterventionType interventionType) {
        this.id = interventionType.getId();
        this.name = interventionType.getName();
        this.interventionSubTypes = interventionType.getInterventionSubTypes().stream().map(InterventionSubTypeDto::new).collect(Collectors.toList());
    }
}
