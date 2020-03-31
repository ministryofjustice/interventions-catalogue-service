package uk.gov.justice.digital.hmpps.interventionscatalogue.event;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.service.SnsService;

@Aspect
@Component
public class InterventionDataEventAspect {

    @Autowired
    SnsService snsService;

    @AfterReturning(pointcut = "@annotation(CreateInterventionDataEvent)", returning = "returnedObject")
    public Object createInterventionDataEvent(JoinPoint joinPoint, Object returnedObject) throws Throwable {
        if (returnedObject instanceof Provider) {
            System.out.println("createInterventionDataEvent");
            snsService.sendEvent((Provider) returnedObject);
        }
        return returnedObject;
    }
}
