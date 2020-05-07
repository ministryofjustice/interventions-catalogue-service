package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface DataEntity {
    Serializable getId();
    LocalDateTime getCreatedDate();
    Long getVersion();
    void setVersion(Long version);
}
