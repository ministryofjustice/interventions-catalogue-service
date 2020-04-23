package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.BaseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataEvent<T> {
    private T entity;
    private DataEventType event;
}
