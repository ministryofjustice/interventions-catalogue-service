package uk.gov.justice.digital.hmpps.interventionscatalogue.service;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

public class GenerateAvroSchema {
    public static void main(String[] args) {
        new GenerateAvroSchema();
    }

    public GenerateAvroSchema() {
        Schema provider = SchemaBuilder.record("AvroProvider")
                .namespace("uk.gov.justice.digital.hmpps.interventionscatalogue.avro")
                .fields()
                .requiredString("id")
                .optionalBoolean("active")
                .optionalString("deliusCode")
                .optionalLong("createdTimestamp")
                .requiredInt("version")
                .optionalString("name")
                .endRecord();

        Schema interventionType = SchemaBuilder.record("AvroInterventionType")
                .namespace("uk.gov.justice.digital.hmpps.interventionscatalogue.avro")
                .fields()
                .requiredString("id")
                .optionalBoolean("active")
                .optionalString("deliusCode")
                .optionalLong("createdTimestamp")
                .requiredInt("version")
                .optionalString("name")
                .endRecord();

        Schema interventionSubType = SchemaBuilder.record("AvroInterventionSubType")
                .namespace("uk.gov.justice.digital.hmpps.interventionscatalogue.avro")
                .fields()
                .requiredString("id")
                .optionalBoolean("active")
                .optionalString("deliusCode")
                .optionalString("deliusParentNsiCode")
                .optionalLong("createdTimestamp")
                .requiredInt("version")
                .optionalString("name")
                .endRecord();


        Schema providerInterventionLink = SchemaBuilder.record("AvroProviderInterventionLink")
                .namespace("uk.gov.justice.digital.hmpps.interventionscatalogue.avro")
                .fields()
                .requiredString("providerId")
                .requiredString("interventionTypeId")
                .requiredString("deliusInterventionCode")
                .requiredString("deliusProviderCode")
                .optionalLong("createdTimestamp")
                .requiredInt("version")
                .endRecord();

        Schema clientIdentifier = SchemaBuilder.record("AvroDataEvent")
                .namespace("uk.gov.justice.digital.hmpps.interventionscatalogue.avro")
                .fields()
                .name("eventType")
                .type().enumeration("EventType")
                .symbols("CREATED", "UPDATED", "DELETED")
                .noDefault()
                .name("entity")
                .type().unionOf()
                .nullType().and()
                .type(provider).and()
                .type(interventionType).and()
                .type(interventionSubType).and()
                .type(providerInterventionLink)
                .endUnion().noDefault()
                .endRecord();

        System.out.println(clientIdentifier.toString(true));
    }
}
