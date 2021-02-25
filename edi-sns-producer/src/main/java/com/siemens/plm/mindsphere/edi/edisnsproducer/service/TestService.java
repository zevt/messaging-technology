package com.siemens.plm.mindsphere.edi.edisnsproducer.service;

import com.amazonaws.services.sns.model.PublishResult;
import com.google.common.base.Preconditions;
import com.siemens.plm.mindsphere.edi.messaging.model.DataInjectMessage;
import com.siemens.plm.mindsphere.edi.messaging.model.EdiMessage;
import com.siemens.plm.mindsphere.edi.messaging.model.FileConverterMessage;
import com.siemens.plm.mindsphere.edi.messaging.model.jobstatus.JobStatusMessage;
import com.siemens.plm.mindsphere.edi.messaging.producer.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;


@Service
@Slf4j
public class TestService {

  private MessageProducer<EdiMessage, PublishResult> messageProducer;
  private String dataInjectFileConverter;
  private String fileConverterSchemaExt;

  @Value("${aws.sns.topic.jobStatus}")
  private String jobStatusTopic;
  public TestService(
      MessageProducer<EdiMessage, PublishResult> messageProducer,
      @Qualifier("DataInjectFileConverter")
      String dataInjectFileConverter,
      @Qualifier("FileConverterSchemaExt")
      String fileConverterSchemaExt) {
    this.messageProducer = messageProducer;
    this.dataInjectFileConverter = dataInjectFileConverter;
    this.fileConverterSchemaExt = fileConverterSchemaExt;
  }

  public void publish(FileConverterMessage message, int numberOfMessages) {
    publish(message, fileConverterSchemaExt, numberOfMessages);
  }


  public void publish(DataInjectMessage message, int numberOfMessages) {
    publish(message, dataInjectFileConverter, numberOfMessages);
  }

  public void publish(JobStatusMessage message, int numberOfMessages) {
    publish(message, jobStatusTopic, numberOfMessages);
  }

  private void publish(EdiMessage message, String topic, int numberOfMessages) {
    Preconditions.checkState(numberOfMessages > 0 , "Number of message must be positive");
    for (int i = 0; i < numberOfMessages; ++i) {
      ListenableFuture<PublishResult> future = this.messageProducer.send(message, topic);
      int k = i;
      future.addCallback(
          r -> log.info("Sent message: {}", k),
          e -> log.error("Failed to send message: {}", k));
    }
  }

}
