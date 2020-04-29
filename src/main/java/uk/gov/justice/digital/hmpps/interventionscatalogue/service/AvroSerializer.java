package uk.gov.justice.digital.hmpps.interventionscatalogue.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.springframework.stereotype.Service;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroDataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.BaseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class AvroSerializer {
    public String serializeAvroDataEventToJSON(
            AvroDataEvent request) throws IOException {

        DatumWriter<AvroDataEvent> writer = new SpecificDatumWriter<>(AvroDataEvent.class);

        String data = new String();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Encoder jsonEncoder = null;
//        try {
            jsonEncoder = EncoderFactory.get().jsonEncoder(AvroDataEvent.getClassSchema(), stream);
            writer.write(request, jsonEncoder);
            jsonEncoder.flush();
            data = stream.toString();
//        } catch (IOException e) {
//            log.error("Serialization error:" + e.getMessage());
//        }
        return data;
    }
}
