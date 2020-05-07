package uk.gov.justice.digital.hmpps.interventionscatalogue.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Component;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.BaseEntity;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionSubTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.ProviderInterventionTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.ProviderRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.SnsService;

import java.util.UUID;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class InterventionDataEventAspect implements Ordered {

    private SnsService snsService;
    private ProviderRepository providerRepository;
    private InterventionTypeRepository interventionTypeRepository;
    private InterventionSubTypeRepository interventionSubTypeRepository;
    private  ProviderInterventionTypeRepository providerInterventionTypeRepository;

    public RevisionRepository<? extends BaseEntity, UUID, Long> getRepositoryForEntity(Object entity) {
        if(entity instanceof Provider) {
            return providerRepository;
        } else if (entity instanceof  InterventionType) {
            return interventionTypeRepository;
        } else if (entity instanceof InterventionSubType) {
            return interventionSubTypeRepository;
        }
//        else if (entity instanceof ProviderInterventionType) {
//            return providerInterventionTypeRepository;
//        }
//        throw new RuntimeException("Unknown entity type");
        return null;
    }

    @AfterReturning(pointcut = "@annotation(CreateInterventionDataEvent)", returning = "returnedObject")
    public Object createInterventionDataEvent(JoinPoint joinPoint, Object returnedObject) throws Throwable {
        if (returnedObject instanceof DataEvent) {
            DataEvent dataEvent  = (DataEvent) returnedObject;

            var repository = getRepositoryForEntity(((DataEvent) returnedObject).getEntity());

            if (repository != null) {
                var revisions = repository.findRevisions((UUID)dataEvent.getEntity().getId());

                Long revisionNumber = revisions.getLatestRevision().getRevisionNumber().get();

                dataEvent.getEntity().setVersion(revisionNumber);
            }

            log.info("createInterventionDataEvent");
            snsService.sendEvent(dataEvent);
        }
        return returnedObject;
    }

    @Override
    public int getOrder() {
        // we need to be higher priority than transactions so we run after them,
        // but running at the highest priority causes an exception
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
