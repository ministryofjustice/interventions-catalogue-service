package uk.gov.justice.digital.hmpps.interventionscatalogue.jpa;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.SerialRevisionEntity;

import java.io.Serializable;

@Slf4j
public class InterventionsRevisionListener implements EntityTrackingRevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        SerialRevisionEntity rev = (SerialRevisionEntity) revisionEntity;
    }

    @Override
    public void entityChanged(Class entityClass, String entityName, Serializable entityId, RevisionType revisionType, Object revisionEntity) {
        log.info("InterventionsRevisionListener entityChanged " + revisionEntity);
    }
}
