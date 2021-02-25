package com.siemens.plm.mindsphere.edi.edisnsproducer.model;


import com.siemens.plm.mindsphere.edi.messaging.model.EdiMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestConfig {
  private int numberOfMessage;
  private EdiMessage message;
}