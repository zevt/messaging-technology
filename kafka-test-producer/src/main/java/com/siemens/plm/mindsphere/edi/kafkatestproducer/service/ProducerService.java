package com.siemens.plm.mindsphere.edi.kafkatestproducer.service;


import com.siemens.plm.mindsphere.edi.kafkatestproducer.KafkaTestProducerApp;
import com.siemens.plm.mindsphere.edi.kafkatestproducer.model.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.util.concurrent.FailureCallback;

@Service
@Slf4j
public class ProducerService {


  private String defaultTopic;
  private MongoTemplate mongoTemplate;
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public ProducerService(
      @Value("${spring.kafka.template.default-topic}")String defaultTopic,
      MongoTemplate mongoTemplate,
      KafkaTemplate<String, String> kafkaTemplate) {
    this.defaultTopic = defaultTopic;
    this.mongoTemplate = mongoTemplate;
    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * If the message failed to send, persist into MongoDB
   * @param message message to send
   * @param topic to send message to
   *
   */
  public void send(KafkaMessage message, String topic) {
    ListenableFuture<SendResult<String, String>> result = this.kafkaTemplate.send(topic, message.toJson());
    SuccessCallback<SendResult<String, String>> success = r -> {
      log.info("successful message: {}", message.toJson());
    };

    FailureCallback failure = (exception)-> {
      String collectionName = "Kafka_Failed_" + KafkaTestProducerApp.instanceId;
      this.mongoTemplate.save(message, collectionName);
      log.debug("Failed to send message: {}, check collection {}",
          message.toJson(), collectionName);
      log.error(exception.getMessage());
    };

    result.addCallback(success, failure);
  }

  public void send(KafkaMessage message) {
    send(message, this.defaultTopic);
  }
}
