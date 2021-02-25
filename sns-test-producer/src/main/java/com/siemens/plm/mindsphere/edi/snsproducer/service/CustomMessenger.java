package com.siemens.plm.mindsphere.edi.snsproducer.service;

import com.amazonaws.services.sns.model.PublishResult;
import com.siemens.plm.mindsphere.edi.messaging.model.EdiMessage;
import com.siemens.plm.mindsphere.edi.messaging.model.jobstatus.JobStatusMessage;
import com.siemens.plm.mindsphere.edi.messaging.producer.MessageProducer;
import com.siemens.plm.mindsphere.edi.snsproducer.model.JobStatusMessagesConfig;
import com.siemens.plm.mindsphere.edi.snsproducer.model.OfferingMessage;
import com.siemens.plm.mindsphere.edi.snsproducer.model.TestConfig;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@Slf4j

public class CustomMessenger {

  @Autowired private MessageProducer<EdiMessage, PublishResult> messageProducer;

  @Value("${sns.topic.custom}")
  private String customTopic;


  public void send(OfferingMessage message, Map<String, String> headers) {
    ListenableFuture<PublishResult> future = this.messageProducer.send(message, headers, customTopic);
	  future.addCallback(
			  p -> log.info("success at message: {}", message),
			  e -> {
				  log.error("failure at message: {}", message);
				  e.printStackTrace();
			  });
  }

}
