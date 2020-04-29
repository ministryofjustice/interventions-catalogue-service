package uk.gov.justice.digital.hmpps.interventionscatalogue.event;

import org.hibernate.HibernateException;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.SnsService;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "interventions.repositoryevents", havingValue = "true")
public class ProviderPersistEventListener implements PersistEventListener {

    @Autowired
    SnsService snsService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @PostConstruct
    private void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.PERSIST).appendListener(this);
    }

    @Override
    public void onPersist(PersistEvent event) throws HibernateException {
        Object entity = event.getObject();

        if (entity instanceof Provider) {
//            snsService.sendEvent((Provider) entity);
        }
    }

    @Override
    public void onPersist(PersistEvent event, Map createdAlready) throws HibernateException {

    }
}
