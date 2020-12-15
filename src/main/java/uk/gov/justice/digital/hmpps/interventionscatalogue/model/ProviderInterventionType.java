package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Audited
public class ProviderInterventionType implements DataEntity {
    @EmbeddedId
    private ProviderInterventionTypeId id;

    @ManyToOne
    @MapsId("providerId")
    private Provider provider;

    @ManyToOne
    @MapsId("interventionTypeId")
    private InterventionType interventionType;

    @CreatedDate
    protected LocalDateTime createdDate;

    @Transient
    protected Long version;
}
