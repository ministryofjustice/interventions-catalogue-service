package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import uk.gov.justice.digital.hmpps.interventionscatalogue.jpa.InterventionsRevisionListener;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "revision_info")
@Entity
@RevisionEntity(InterventionsRevisionListener.class)
public class SerialRevisionEntity {
    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="revision_info_id_sequence", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @RevisionNumber
    private Long id;

    @RevisionTimestamp
    private long timestamp;

    private String username;
}
