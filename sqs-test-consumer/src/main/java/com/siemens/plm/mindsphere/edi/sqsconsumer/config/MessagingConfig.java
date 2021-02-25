package com.siemens.plm.mindsphere.edi.sqsconsumer.config;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.siemens.plm.mindsphere.edi.messaging.consumer.TaskExecutor;
import com.siemens.plm.mindsphere.edi.messaging.consumer.TaskFactory;
import com.siemens.plm.mindsphere.edi.sqsconsumer.task.SqsTaskFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {


  @Autowired
  private AmazonSQS amazonSQS;

  @Value("${aws.sqs.queue}")
  private String queue;

//  @Bean
//  public String queueName() {
//    return queue;
//  }
  private static TaskExecutor taskExecutor;

  @Bean(name = "TaskExecutor")
  public TaskExecutor taskExecutor() {
    if (taskExecutor != null)
      return taskExecutor;
    return new TaskExecutor(1);
  }

  @Bean(name = "TaskFactory")
  public TaskFactory<Message> taskFactory() {
    return new SqsTaskFactory(amazonSQS, queue);
  }

}
