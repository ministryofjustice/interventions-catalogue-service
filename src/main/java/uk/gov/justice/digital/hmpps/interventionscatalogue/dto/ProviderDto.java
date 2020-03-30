package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderDto {
    private UUID id;
    private String name;
    private LocalDateTime createdDate;

    public ProviderDto(Provider provider) {
        this.id = provider.getId();
        this.name = provider.getName();
        this.createdDate = provider.getCreatedDate();
    }
}
