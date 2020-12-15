package uk.gov.justice.digital.hmpps.interventionscatalogue.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.history.Revision;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.InterventionSubTypeResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.InterventionTypeResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.ProviderTypeLinkResponse;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.RevisionDto;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.ProviderInterventionType;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Mapper
public interface ProviderMapper {
    ProviderMapper INSTANCE = Mappers.getMapper(ProviderMapper.class);

    ProviderDto map(Provider provider);

    Set<ProviderDto> map(Iterable<Provider> providers);

    List<RevisionDto> map(Stream<Revision<Long, Provider>> revisions);

    @Mapping(source = "requiredRevisionNumber", target = "revisionNumber")
    @Mapping(source = "requiredRevisionInstant", target = "revisionInstant")
    @Mapping(source = "metadata.revisionType", target = "revisionType")
    RevisionDto map(Revision<Long, Provider> revision);

    List<InterventionTypeResponse> map(List<InterventionType> interventionTypes);

    InterventionTypeResponse map(InterventionType interventionType);

    List<InterventionSubTypeResponse> map(Set<InterventionSubType> interventionSubTypes);

    InterventionSubTypeResponse map(InterventionSubType interventionSubType);

    @Mapping(source = "provider.id", target = "providerId")
    @Mapping(source = "interventionType.id", target = "interventionTypeId")
    @Mapping(source = "interventionType.deliusCode", target = "deliusInterventionCode")
    @Mapping(source = "provider.deliusCode", target = "deliusProviderCode")
    ProviderTypeLinkResponse map(ProviderInterventionType providerInterventionType);
}
