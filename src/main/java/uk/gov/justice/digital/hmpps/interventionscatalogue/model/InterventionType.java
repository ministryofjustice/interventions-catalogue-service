package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class InterventionType {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private UUID id;
    private String name;

    @ManyToMany
    @JoinTable(name = "provider_intervention_type", joinColumns = @JoinColumn(name="intervention_type_id"), inverseJoinColumns = @JoinColumn(name="provider_id"))
    private Set<Provider> providers;

    @OneToMany(mappedBy = "interventionType")
    private Set<InterventionSubType> interventionSubTypes;
}
