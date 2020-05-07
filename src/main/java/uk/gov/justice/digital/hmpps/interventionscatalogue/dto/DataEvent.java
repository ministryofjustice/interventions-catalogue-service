package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.BaseEntity;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.DataEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataEvent<T extends DataEntity> {
    private T entity;
    private DataEventType event;
}

