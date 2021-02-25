package com.siemens.plm.mindsphere.edi.snsproducer.model;

import com.siemens.plm.mindsphere.edi.messaging.model.EdiMessage;
import lombok.Data;

@Data
public class TestMessage implements EdiMessage {

  private String text;

  @Override
  public String toJson() {
    return text;
  }
}