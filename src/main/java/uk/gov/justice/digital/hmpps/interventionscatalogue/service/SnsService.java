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
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.BaseEntity;
import uk.gov.justice.digital.hmpps.interventionscatalogue.model.Provider;

@Service
@Slf4j
public class SnsService {

    private final NotificationMessagingTemplate topicTemplate;
    private final AmazonSNSAsync amazonSns;
    private final String topicArn;
    private final ObjectMapper objectMapper;

    public SnsService(@Qualifier("awsSnsClient") final AmazonSNSAsync amazonSns,
                      @Value("${sns.topic.arn}") final String topicArn,
                      final ObjectMapper objectMapper) {

        this.topicTemplate = new NotificationMessagingTemplate(amazonSns);
        this.topicArn = topicArn;
        this.amazonSns = amazonSns;
        this.objectMapper = objectMapper;
    }

    public void sendEvent(final BaseEntity payload) {
        try {
            log.info("Sending message");
            topicTemplate.convertAndSend(
                    new TopicMessageChannel(amazonSns, topicArn),
                    objectMapper.writeValueAsString(payload)
            );
        } catch (JsonProcessingException e) {
            log.error("Failed to convert payload {} to json", payload);
        }
    }
}
