package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class InterventionType {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    UUID id;
    String name;

    @ManyToMany
    @JoinTable(name = "provider_intervention_type", joinColumns = @JoinColumn(name="intervention_type_id"), inverseJoinColumns = @JoinColumn(name="provider_id"))
    Set<Provider> providers;

    @OneToMany(mappedBy = "interventionType")
    Set<InterventionSubType> interventionSubTypes;
}
