package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({AuditingEntityListener.class})
@Audited
public class InterventionType extends BaseEntity {
    private String name;

    private String deliusCode;

    private Boolean active;

    @Singular
    @ManyToMany
    @JoinTable(name = "provider_intervention_type", joinColumns = @JoinColumn(name="intervention_type_id"), inverseJoinColumns = @JoinColumn(name="provider_id"))
    @EqualsAndHashCode.Exclude()
    @ToString.Exclude
    private Set<Provider> providers;

    @Singular
    @OneToMany(mappedBy = "interventionType")
    private Set<InterventionSubType> interventionSubTypes;
}
