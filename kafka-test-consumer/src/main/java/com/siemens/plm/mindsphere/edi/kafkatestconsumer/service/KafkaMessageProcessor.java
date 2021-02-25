package com.siemens.plm.mindsphere.edi.kafkatestconsumer.service;

import com.siemens.plm.mindsphere.edi.concurrency.model.KafkaMessage;
import com.siemens.plm.mindsphere.edi.kafkatestconsumer.KafkaTestConsumerApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaMessageProcessor {

  private MongoTemplate mongoTemplate;

  @Autowired
  public KafkaMessageProcessor(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public void process(KafkaMessage message) {
    message.setConsumerInstanceId(KafkaTestConsumerApp.instanceId);
    this.mongoTemplate.save(message, message.getTestId());
  }
}
