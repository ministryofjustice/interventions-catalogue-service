package uk.gov.justice.digital.hmpps.interventionscatalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@SuperBuilder
public class CreateProviderResponse extends Provider {
    public CreateProviderResponse(Provider provider) {
        this.setId(provider.getId());
        this.setCreatedDate(provider.getCreatedDate());
        this.setInterventionTypes(provider.getInterventionTypes());
        this.setName(provider.getName());
    }
}
