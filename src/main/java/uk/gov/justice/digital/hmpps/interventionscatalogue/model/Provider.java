package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners({AuditingEntityListener.class})
@Audited
public class Provider extends BaseEntity {
    protected String name;

    protected String deliusCode;

    protected Boolean active;

    @Singular
    @OneToMany(mappedBy = "provider")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected Set<ProviderInterventionType> providerInterventionTypes;
}
