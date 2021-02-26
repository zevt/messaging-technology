package com.zeroexception.kafkatestconsumer.service;

import com.zeroexception.kafkatestconsumer.model.KafkaMessage;
import com.zeroexception.kafkatestconsumer.KafkaTestConsumerApp;
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
