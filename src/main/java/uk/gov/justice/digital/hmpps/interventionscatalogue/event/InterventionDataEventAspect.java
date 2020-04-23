package uk.gov.justice.digital.hmpps.interventionscatalogue.event;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Component;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.BaseEntity;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionSubTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.InterventionTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.repository.ProviderRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.SnsService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class InterventionDataEventAspect implements Ordered {

    private SnsService snsService;

    private Map<Class<? extends BaseEntity>, RevisionRepository<? extends BaseEntity, UUID, Long>> repositoryMap;

    public InterventionDataEventAspect(SnsService snsService, ProviderRepository providerRepository, InterventionTypeRepository interventionTypeRepository, InterventionSubTypeRepository interventionSubTypeRepository) {
        this.snsService = snsService;

        repositoryMap = new HashMap<>();
        repositoryMap.put(Provider.class, providerRepository);
        repositoryMap.put(InterventionType.class, interventionTypeRepository);
        repositoryMap.put(InterventionSubType.class, interventionSubTypeRepository);
    }

    @AfterReturning(pointcut = "@annotation(CreateInterventionDataEvent)", returning = "returnedObject")
    public Object createInterventionDataEvent(JoinPoint joinPoint, Object returnedObject) throws Throwable {
        if (returnedObject instanceof DataEvent) {

            BaseEntity entity = ((DataEvent<BaseEntity>)returnedObject).getEntity();

            RevisionRepository<? extends BaseEntity, UUID, Long> repository = repositoryMap.get(entity.getClass());

            var revisions = repository.findRevisions(entity.getId());
            Long revisionNumber = revisions.getLatestRevision().getRevisionNumber().get();

            entity.setVersion(revisionNumber);

            log.info("createInterventionDataEvent");
            snsService.sendEvent(entity);
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
