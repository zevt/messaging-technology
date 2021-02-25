package com.siemens.plm.mindsphere.edi.snsproducer.model;


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
  private TestMessage message;
}

