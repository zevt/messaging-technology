package com.siemens.plm.mindsphere.edi.snsproducer.config;


import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.siemens.plm.mindsphere.edi.messaging.aws.producer.SnsMessageProducer;
import com.siemens.plm.mindsphere.edi.messaging.model.EdiMessage;
import com.siemens.plm.mindsphere.edi.messaging.producer.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MessagingConfig {

  private String defaultTopic;
  private AmazonSNS amazonSNS;

  public MessagingConfig(
      @Value("${aws.sns.topic}")String defaultTopic,
      AmazonSNS amazonSNS) {
    this.defaultTopic = defaultTopic;
    this.amazonSNS = amazonSNS;
  }

  @Bean(name = "MessageProducer")
  public MessageProducer<EdiMessage, PublishResult> messageProducer() {
    return new SnsMessageProducer(defaultTopic, amazonSNS);
  }


}
