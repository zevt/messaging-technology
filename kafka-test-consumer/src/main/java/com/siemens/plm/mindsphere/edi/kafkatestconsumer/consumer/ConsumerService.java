package com.siemens.plm.mindsphere.edi.kafkatestconsumer.consumer;

import com.google.gson.Gson;
import com.siemens.plm.mindsphere.edi.concurrency.model.KafkaMessage;
import com.siemens.plm.mindsphere.edi.commons.messaging.consumer.task.Task;
import com.siemens.plm.mindsphere.edi.commons.messaging.consumer.task.TaskExecutor;
import com.siemens.plm.mindsphere.edi.commons.messaging.consumer.task.TaskFactory;
import com.siemens.plm.mindsphere.edi.kafkatestconsumer.service.KafkaMessageProcessor;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;


@Configuration
@EnableKafka
@Slf4j
public class ConsumerService {

//  private KafkaTestProcessor testProcessor;
  private KafkaMessageProcessor messageProcessor;
  private TaskExecutor taskExecutor;
  private TaskFactory<String, Object> taskFactory;

  @Autowired
  public ConsumerService(
//      KafkaTestProcessor testProcessor,
      KafkaMessageProcessor messageProcessor,
      @Qualifier("TaskExecutor") TaskExecutor taskExecutor,
      TaskFactory<String, Object> taskFactory) {
//    this.testProcessor = testProcessor;
    this.messageProcessor = messageProcessor;
    this.taskExecutor = taskExecutor;
    this.taskFactory = taskFactory;
  }

  @KafkaListener(topics = "${spring.kafka.template.default-topic}")
  public void listen(ConsumerRecord<?, ?> record) {
    log.info("Received message with topic: {}", record.topic());
    log.info("Message: {}", record.value());
    KafkaMessage message = new Gson()
        .fromJson(record.value().toString(), KafkaMessage.class);
    message.setSendAt(record.timestamp());
    message.setReceiveAt(Instant.now().toEpochMilli());
    messageProcessor.process(message);
//    latch1.countDown();
  }

  @KafkaListener(topics = "kafka-test")
  public void listen1(ConsumerRecord<?, ?> record) {
    log.info("Received message with topic: {}", record.topic());
    log.info("Message: {}", record.value());
//    testProcessor.pushMessage(record.value().toString());

    Task task = this.taskFactory.create(record.value().toString());
    this.taskExecutor.push(task);

  }
}
