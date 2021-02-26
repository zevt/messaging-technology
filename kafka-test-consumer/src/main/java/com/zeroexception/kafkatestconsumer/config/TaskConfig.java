package com.zeroexception.kafkatestconsumer.config;

import com.zeroexception.commons.messaging.consumer.task.TaskExecutor;
import com.zeroexception.kafkatestconsumer.task.ConsumerTaskFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig {

  @Bean(name = "TaskExecutor")
  public TaskExecutor taskExecutor() {
    return new TaskExecutor(1);
  }

  @Bean
  public ConsumerTaskFactory taskFactory() {
    return new ConsumerTaskFactory();
  }


}
