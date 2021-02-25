package com.siemens.plm.mindsphere.edi.concurrency.model;


import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * All fields are epoch timestamps in milli-second
 */
@Data
public class KafkaMessage {

  @Id
  private String id;
  private String producerInstanceId;
  private String consumerInstanceId;
  private String text;
  private String testId;
  private long createdAt;
  private long sendAt;
  private long producerLatency;
  private long receiveAt;
  private long kafkaLatency;

  public void setReceiveAt(long receivedAt) {
    this.receiveAt = receivedAt;
    this.kafkaLatency = this.receiveAt - this.sendAt;
    this.producerLatency = this.sendAt - this.createdAt;
  }


}
