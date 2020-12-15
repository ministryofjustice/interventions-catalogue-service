package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners({AuditingEntityListener.class})
@Audited
public class InterventionSubType extends BaseEntity {
    private String name;

    private String deliusCode;

    private Boolean active;

    @ManyToOne
    @EqualsAndHashCode.Exclude()
    @ToString.Exclude
    private InterventionType interventionType;
}
