package com.zeroexception.commons.messaging.test.service;

import com.zeroexception.commons.messaging.producer.MessageProducer;
import com.zeroexception.commons.messaging.test.model.Message;
import com.zeroexception.commons.messaging.test.model.TestConfig;
import com.siemens.plm.mindsphere.edi.messaging.model.EdiMessage;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
public class TestMessagingService {

  private String defaultTopic;
  private MessageProducer<String, String> producer;
  private ScheduledService scheduler;

  public TestMessagingService(
      String defaultTopic,
      MessageProducer<String, String> producer, ScheduledService scheduler) {
    this.defaultTopic = defaultTopic;
    this.producer = producer;
    this.scheduler = scheduler;
  }


  /**
   * Run test based on TestConfig
   *
   * @param config
   */
  public void runTest(TestConfig config) {
    runTest(config, this.defaultTopic);
  }

  public void runTest(TestConfig config, String topic) {
    int[] arr = Arrays.copyOf(new int[0], config.getMessagePerPeriod());
    Runnable task =
        () -> {
          for (int i = 0; i < arr.length; ++i) {
            log.info("index : {}", i);
            Message message = new Message();
            message.setTestId(config.getTestId());
            message.setText(config.getText());
            ListenableFuture future  = this.producer.send(message.toJson(), topic);
            future.addCallback(e-> {}, Throwable::printStackTrace);
          }
        };

    this.scheduler.schedule(task, config.getPeriod(), config.getRepetition());
  }

  public void runTest(TestConfig config, String topic, EdiMessage message) {
    int[] arr = Arrays.copyOf(new int[0], config.getMessagePerPeriod());
    Runnable task =
        () -> {
          for (int i = 0; i < arr.length; ++i) {
            log.info("index : {}", i);
            ListenableFuture future  = this.producer.send(message.toJson(), topic);
            future.addCallback(e-> {}, Throwable::printStackTrace);
          }
        };

    this.scheduler.schedule(task, config.getPeriod(), config.getRepetition());
  }

  public void runTest(TestConfig config, EdiMessage message) {
    int[] arr = Arrays.copyOf(new int[0], config.getMessagePerPeriod());
    Runnable task =
        () -> {
          for (int i = 0; i < arr.length; ++i) {
            log.info("index : {}", i);
            ListenableFuture future  = this.producer.send(message.toJson());
            future.addCallback(e-> {}, Throwable::printStackTrace);
          }
        };

    this.scheduler.schedule(task, config.getPeriod(), config.getRepetition());
  }

}
