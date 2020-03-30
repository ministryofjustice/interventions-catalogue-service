package uk.gov.justice.digital.hmpps.interventionscatalogue.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({AuditingEntityListener.class})
public class Provider {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private UUID id;

    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private UUID version;

    private String name;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToMany(mappedBy = "interventionSubTypes")
    private Set<InterventionType> interventionTypes;
}
