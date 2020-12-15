package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.history.RevisionMetadata;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RevisionDto {
    Long revisionNumber;
    Instant revisionInstant;

    //TODO: Waiting for this to be released: https://github.com/spring-projects/spring-data-envers/issues/215
    String revisionType;
}
