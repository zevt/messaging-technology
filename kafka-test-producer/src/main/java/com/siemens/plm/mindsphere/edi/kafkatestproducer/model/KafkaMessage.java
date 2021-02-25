package com.siemens.plm.mindsphere.edi.kafkatestproducer.model;


import com.google.gson.Gson;
import com.siemens.plm.mindsphere.edi.kafkatestproducer.KafkaTestProducerApp;
import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * All fields are epoch timestamps in milli-second
 */
@Document
@TypeAlias("KafkaMessage")
@Data
public class KafkaMessage {

  @Id
  private String id;
  private long createdAt;
  private String testId;
  private String producerInstanceId;
  private String text;

  public KafkaMessage() {
    createdAt = Instant.now().toEpochMilli();
    producerInstanceId = KafkaTestProducerApp.instanceId;
  }
  public String toJson() {
    return new Gson().toJson(this);
  }

}
