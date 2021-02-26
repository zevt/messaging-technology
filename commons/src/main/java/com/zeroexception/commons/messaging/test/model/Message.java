package com.zeroexception.commons.messaging.test.model;


import com.google.gson.Gson;
import java.time.Instant;
import lombok.Data;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.TypeAlias;
//import org.springframework.data.mongodb.core.mapping.Document;

/**
 * All fields are epoch timestamps in milli-second
 */
//@Document
//@TypeAlias("Message")
@Data
public class Message {

//  @Id
  private String id;
  private long createdAt;
  private String testId;
  private String producerInstanceId;
  private String text;

  public Message() {
    createdAt = Instant.now().toEpochMilli();
  }
  public String toJson() {
    return new Gson().toJson(this);
  }

}
