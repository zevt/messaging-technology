package com.zeroexception.kafkatestproducer.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * The KafkaProducerConfig class is used to generate beans of: KafkaTemplate KafkaProducer
 */
@Configuration
public class KafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private List<String> bootstrapServers;

  @Value("${spring.kafka.producer.acks:-1}")
  private String acks;

  @Value("${spring.kafka.producer.retries}")
  private int retries;

  @Value("${spring.kafka.producer.batch-size}")
  private int batchSize;

  @Value("${spring.kafka.producer.linger.ms}")
  private int lingerms;

  @Value("${spring.kafka.producer.buffer-memory}")
  private long bufferMemory;

  @Value("${kafka.producer.max.block.ms}")
  private long maxBlockTime;

  @Value("${spring.kafka.producer.key-serializer}")
  private String keySerializer;

  @Value("${spring.kafka.producer.value-serializer}")
  private String valueSerializer;

  @Value("${spring.kafka.producer.compression-type}")
  private String compressionType;

  @Value("${kafka.send.buffer.bytes}")
  private String sendBufferByte;

  @Value("${kafka.max.request.size}")
  private String maxRequestSize;

  @Bean
  public Producer<String, String> producer() {
    return new KafkaProducer<>(this.propertiesConfig());
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(this.producerFactory());
  }

  private ProducerFactory<String, String> producerFactory() {
    return new DefaultKafkaProducerFactory<>(propertiesConfig());
  }

  private Map<String, Object> propertiesConfig() {

    Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
    configs.put(ProducerConfig.ACKS_CONFIG, this.acks);
    configs.put(ProducerConfig.RETRIES_CONFIG, this.retries);
    configs.put(ProducerConfig.BATCH_SIZE_CONFIG, this.batchSize);
    configs.put(ProducerConfig.LINGER_MS_CONFIG, this.lingerms);
    configs.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, this.maxBlockTime);
    configs.put(ProducerConfig.BUFFER_MEMORY_CONFIG, this.bufferMemory);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, this.keySerializer);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, this.valueSerializer);
    configs.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, this.compressionType);
    configs.put(ProducerConfig.SEND_BUFFER_CONFIG, this.sendBufferByte);
    configs.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, this.maxRequestSize);

    return configs;
  }


}
