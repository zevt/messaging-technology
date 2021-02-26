package com.zeroexception.kafkatestproducer;

import com.zeroexception.kafkatestproducer.model.InstanceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class KafkaTestProducerApp implements CommandLineRunner {

  public static String instanceId;

  @Autowired
  private MongoTemplate mongoTemplate;

  public static void main(String[] args) {
    SpringApplication.run(KafkaTestProducerApp.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    InstanceConfig config = new InstanceConfig();
    InstanceConfig createdConfig = this.mongoTemplate.save(config);
    instanceId = createdConfig.getId();
  }
}
