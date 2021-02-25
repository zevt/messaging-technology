package com.siemens.plm.mindsphere.edi.sqsconsumer.consumer.jobstatus;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.google.gson.JsonObject;
import com.siemens.plm.mindsphere.edi.messaging.aws.util.AWSMessageConverter;
import com.siemens.plm.mindsphere.edi.messaging.aws.util.SQSUtils;
import com.siemens.plm.mindsphere.edi.messaging.consumer.Task;
import com.siemens.plm.mindsphere.edi.messaging.consumer.TaskExecutor;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class JobStatusConsumer {

  @Autowired private TaskExecutor taskExecutor;

  @Autowired private JobStatusTaskFactory taskFactory;

  @Value("${aws.sqs.queue.custom}")
  private String queue;
  @Value("${aws.sqs.queue.job-status}")
  private String jobStatusQueue;

  @Autowired private AmazonSQS amazonSQS;

  @Scheduled(fixedDelay = 150)
  public void consume() throws InterruptedException {
    if (taskExecutor.getAvailableThreads() > 0) {
      int size = Math.min(taskExecutor.getAvailableThreads(), 10);
      List<Message> messageList = SQSUtils.receiveMessage(amazonSQS, queue, size);
      if (!messageList.isEmpty()) {
        messageList.forEach(
            m -> {
              SQSUtils.deleteMessage(amazonSQS, queue, m);
              JsonObject object = AWSMessageConverter.extractPayload(m, JsonObject.class);
              Map<String, String> headers = AWSMessageConverter.extractHeader(m);
              log.info("Message payload: {}", object);
              log.info("Raw headers: {}", headers);
            });
      } else {
        Thread.sleep(100L);
      }
    }
  }

  @Scheduled(fixedDelay = 100)
  public void consumeJobStatus() throws InterruptedException {
    if (taskExecutor.getAvailableThreads() > 0) {
      int size = Math.min(taskExecutor.getAvailableThreads(), 10);
      List<Message> messageList = SQSUtils.receiveMessage(amazonSQS, jobStatusQueue, size);
      if (!messageList.isEmpty()) {
        messageList.forEach(
            m -> {
              log.info("Raw message: {}", m);
              Task task = taskFactory.create(m);
              taskExecutor.submit(task);
            });
      } else {
        Thread.sleep(100L);
      }
    }
  }
}
