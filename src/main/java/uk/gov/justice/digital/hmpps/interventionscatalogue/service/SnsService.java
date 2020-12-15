package uk.gov.justice.digital.hmpps.interventionscatalogue.service;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.TopicMessageChannel;
import org.springframework.stereotype.Service;
import uk.gov.justice.digital.hmpps.interventionscatalogue.dto.DataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.mappers.AutoAvroMapper;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.BaseEntity;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.DataEntity;

import java.io.IOException;

@Service
@Slf4j
public class SnsService {
    @Value( "${interventions.snsEnabled:false}" )
    private Boolean snsEnabled;

    private final NotificationMessagingTemplate topicTemplate;
    private final AmazonSNSAsync amazonSns;
    private final String topicArn;
    private final AvroSerializer avroSerializer;

    public SnsService(@Qualifier("awsSnsClient") final AmazonSNSAsync amazonSns,
                      @Value("${sns.topic.arn}") final String topicArn,
                      final AvroSerializer avroSerializer) {

        this.topicTemplate = new NotificationMessagingTemplate(amazonSns);
        this.topicArn = topicArn;
        this.amazonSns = amazonSns;
        this.avroSerializer = avroSerializer;
    }

    public void sendEvent(final DataEvent<? extends DataEntity> payload) throws IOException {
        if (snsEnabled) {
            log.info("Sending message");
            topicTemplate.convertAndSend(
                    new TopicMessageChannel(amazonSns, topicArn),
                    avroSerializer.serializeAvroDataEventToJSON(AutoAvroMapper.INSTANCE.map(payload))
            );
        }
    }
}
