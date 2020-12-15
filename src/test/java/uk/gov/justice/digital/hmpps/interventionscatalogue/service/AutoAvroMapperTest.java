package uk.gov.justice.digital.hmpps.interventionscatalogue.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroDataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.EventType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEventType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.mappers.AutoAvroMapper;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.InterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AutoAvroMapperTest {

    @Test
    void testMappingAndSerialisingProvider() {
        DataEvent<Provider> input = new DataEvent<>(Provider.builder()
                .id(UUID.randomUUID())
                .name("Test Provider")
                .deliusCode("TEST1")
                .active(true)
                .version(86L)
                .createdDate(LocalDateTime.now()).build(), DataEventType.CREATED);
        AvroDataEvent output = AutoAvroMapper.INSTANCE.map(input);
        assertThat(output.getEventType()).isEqualTo(EventType.CREATED);
        assertThat(output.getEntity()).isInstanceOf(AvroProvider.class);
    }

    @Test
    void testMappingAndSerialisingInterventionType() {
        DataEvent<InterventionType> input = new DataEvent<>(InterventionType.builder()
                .id(UUID.randomUUID())
                .name("Test Intervention Type")
                .deliusCode("TEST1")
                .active(true)
                .version(86L)
                .createdDate(LocalDateTime.now()).build(), DataEventType.CREATED);
        AvroDataEvent output = AutoAvroMapper.INSTANCE.map(input);
        assertThat(output.getEventType()).isEqualTo(EventType.CREATED);
    }

    @Test
    void testMappingAndSerialisingInterventionSubType() {
        DataEvent<InterventionSubType> input = new DataEvent<>(InterventionSubType.builder()
                .id(UUID.randomUUID())
                .name("Test Intervention Type")
                .deliusCode("TEST1")
                .active(true)
                .version(86L)
                .createdDate(LocalDateTime.now())
                .interventionType(InterventionType.builder().id(UUID.randomUUID()).build()).build(), DataEventType.CREATED);
        AvroDataEvent output = AutoAvroMapper.INSTANCE.map(input);
        assertThat(output.getEventType()).isEqualTo(EventType.CREATED);
    }
}
