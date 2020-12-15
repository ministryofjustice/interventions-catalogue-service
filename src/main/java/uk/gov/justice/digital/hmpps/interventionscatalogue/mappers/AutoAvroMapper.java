package uk.gov.justice.digital.hmpps.interventionscatalogue.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroDataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroInterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroProviderInterventionLink;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderTypeLinkResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.ProviderInterventionType;

import java.time.LocalDateTime;

@Mapper
public abstract class AutoAvroMapper {
    public static AutoAvroMapper INSTANCE = Mappers.getMapper(AutoAvroMapper.class);

    @Mapping(source = "entity", target = "entity", qualifiedByName = "baseEntityTransformation")
    @Mapping(source = "event", target = "eventType")
    public abstract AvroDataEvent map(DataEvent<?> entity);

    /**
     * MapStruct is an Annotation Processor that generates code during compilation.
     * We don't generate any reflection checks (All the types that implement and
     * interface or extend a class are not known during compilation).
     * Therefore this approach is needed.
     *
     * @param entity
     * @return
     */
    @Named("baseEntityTransformation")
    Object mapBaseEntity(Object entity) {
        if (entity instanceof Provider) {
            return mapProvider((Provider) entity);
        } else if (entity instanceof InterventionType) {
            return mapInterventionType((InterventionType) entity);
        } else if (entity instanceof InterventionSubType) {
            return mapInterventionSubType((InterventionSubType) entity);
        } else if (entity instanceof ProviderInterventionType) {
            return mapProviderTypeLink((ProviderInterventionType) entity);
        }
        return null;
    }

    @Mapping(source = "createdDate", target = "createdTimestamp" )
    abstract AvroProvider mapProvider(Provider provider);

    @Mapping(source = "createdDate", target = "createdTimestamp" )
    abstract AvroInterventionType mapInterventionType(InterventionType interventionType);

    @Mapping(source = "createdDate", target = "createdTimestamp" )
    @Mapping(source = "interventionType.deliusCode", target = "deliusParentNsiCode")
    abstract AvroInterventionSubType mapInterventionSubType(InterventionSubType interventionSubType);

    @Mapping(source = "provider.id", target = "providerId")
    @Mapping(source = "provider.deliusCode", target = "deliusProviderCode")
    @Mapping(source = "interventionType.id", target = "interventionTypeId")
    @Mapping(source = "interventionType.deliusCode", target = "deliusInterventionCode")
    @Mapping(source = "createdDate", target = "createdTimestamp" )
    abstract AvroProviderInterventionLink mapProviderTypeLink(ProviderInterventionType providerInterventionType);

    String map(java.util.UUID value) {
        return value.toString();
    };

    Long map(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toInstant(java.time.ZoneOffset.UTC).getEpochSecond();
    }
}
