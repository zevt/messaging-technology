package com.siemens.plm.mindsphere.edi.kafkatestproducer.service;

import com.siemens.plm.mindsphere.edi.kafkatestproducer.model.KafkaMessage;
import com.siemens.plm.mindsphere.edi.kafkatestproducer.model.TestConfig;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaTestService {

  private String defaultTopic;
  private ProducerService producer;
  private ScheduledService scheduler;
  AtomicLong counter = new AtomicLong(0);
  @Autowired
  public KafkaTestService(
      @Value("${spring.kafka.template.default-topic}")String defaultTopic,
      ProducerService producer, ScheduledService scheduler) {
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
          for (int i: arr) {
            log.info("Counter : {}", counter.incrementAndGet());
            KafkaMessage message = new KafkaMessage();
            message.setTestId(config.getTestId());
            message.setText(counter.get() + ": " + config.getText());
            this.producer.send(message, topic);
          }
        };

    this.scheduler.schedule(task, config.getPeriod(), config.getRepetition());
  }
}
