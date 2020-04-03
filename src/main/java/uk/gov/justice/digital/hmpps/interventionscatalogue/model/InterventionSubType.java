package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited(targetAuditMode = NOT_AUDITED)
public class InterventionSubType {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private UUID id;
    private String name;

    @ManyToOne
    private InterventionType interventionType;
}
