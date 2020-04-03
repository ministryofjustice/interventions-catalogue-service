package uk.gov.justice.digital.hmpps.interventionscatalogue.jpa;

import org.hibernate.envers.RevisionListener;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.SerialRevisionEntity;

public class InterventionsRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        SerialRevisionEntity rev = (SerialRevisionEntity) revisionEntity;
    }
}
