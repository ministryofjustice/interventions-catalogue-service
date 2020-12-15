package uk.gov.justice.digital.hmpps.interventionscatalogue.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.data.history.Revisions;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Component;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.DataEntity;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.ProviderInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionSubTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.ProviderInterventionTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.SnsService;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class InterventionDataEventAspect implements Ordered {

    private final SnsService snsService;
    private final RevisionRepository<Provider, UUID, Long> providerRepository;
    private final InterventionTypeRepository interventionTypeRepository;
    private final InterventionSubTypeRepository interventionSubTypeRepository;
    private final ProviderInterventionTypeRepository providerInterventionTypeRepository;

    public Revisions<Long, ? extends DataEntity> getRevisionsForEntity(DataEntity entity) {
        if(entity instanceof Provider) {
            return providerRepository.findRevisions(((Provider) entity).getId());
        } else if (entity instanceof  InterventionType) {
            return interventionTypeRepository.findRevisions(((InterventionType) entity).getId());
        } else if (entity instanceof InterventionSubType) {
            return interventionSubTypeRepository.findRevisions(((InterventionSubType) entity).getId());
        } else if (entity instanceof ProviderInterventionType) {
            return providerInterventionTypeRepository.findRevisions(((ProviderInterventionType) entity).getId());
        }

        return null;
    }

    @AfterReturning(pointcut = "@annotation(CreateInterventionDataEvent)", returning = "returnedObject")
    public Object createInterventionDataEvent(JoinPoint joinPoint, Object returnedObject) throws Throwable {
        if (returnedObject instanceof DataEvent) {
            var dataEvent = (DataEvent<?>) returnedObject;

            Optional <Long> revision = getRevisionsForEntity(dataEvent.getEntity()).getLatestRevision().getRevisionNumber();

            revision.ifPresent(rev -> dataEvent.getEntity().setVersion(rev));

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
