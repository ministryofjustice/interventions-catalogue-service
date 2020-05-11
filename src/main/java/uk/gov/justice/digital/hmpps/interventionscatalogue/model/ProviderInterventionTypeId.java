package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProviderInterventionTypeId implements Serializable {
    private UUID providerId;
    private UUID interventionTypeId;
}

