package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners({AuditingEntityListener.class})
@Audited
public class Provider extends BaseEntity {
    protected String name;

    @Singular
    @ManyToMany(mappedBy = "providers")
    protected Set<InterventionType> interventionTypes;
}
