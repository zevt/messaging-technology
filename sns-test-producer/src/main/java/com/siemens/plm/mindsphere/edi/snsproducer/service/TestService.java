package com.siemens.plm.mindsphere.edi.snsproducer.service;

import com.amazonaws.services.sns.model.PublishResult;
import com.siemens.plm.mindsphere.edi.messaging.model.EdiMessage;
import com.siemens.plm.mindsphere.edi.messaging.model.jobstatus.JobStatusMessage;
import com.siemens.plm.mindsphere.edi.messaging.producer.MessageProducer;
import com.siemens.plm.mindsphere.edi.snsproducer.model.JobStatusMessagesConfig;
import com.siemens.plm.mindsphere.edi.snsproducer.model.TestConfig;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@Slf4j
public class TestService {

  @Autowired private MessageProducer<EdiMessage, PublishResult> messageProducer;

  public void send(TestConfig config) {
    for (int i = 0; i < config.getNumberOfMessage(); ++i) {
      Map<String, String> headers = new HashMap<>();
      headers.put("traceId", "xxxxxxxxxxxxxxxxx");
      headers.put("spanId", "yyyyyyyyyyyyyyyy");
      ListenableFuture<PublishResult> future = messageProducer.send(config.getMessage(), headers);
      int k = i;
      future.addCallback(
          p -> log.info("success at message: {}", k),
          e -> {
            log.error("failure at message: {}", k);
            e.printStackTrace();
          });
    }
  }

  public void send(JobStatusMessagesConfig config) {
    String jobId = Instant.now().toString();
    for (JobStatusMessage message: config.getMessages()) {
      message.setJobId(jobId);
      Map<String, String> headers = new HashMap<>();
      headers.put("traceId", "xxxxxxxxxxxxxxxxx");
      headers.put("spanId", "yyyyyyyyyyyyyyyy");
      ListenableFuture<PublishResult> future = messageProducer.send(message, headers);
      future.addCallback(
          p -> log.info("success at message: {}", message),
          e -> {
            log.error("failure at message: {}", message);
            e.printStackTrace();
          });
    }
  }
}
