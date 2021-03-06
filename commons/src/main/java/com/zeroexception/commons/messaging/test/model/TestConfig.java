package com.zeroexception.commons.messaging.test.model;


import lombok.Data;

@Data
public class TestConfig {
  private String testId;
  private int messagePerPeriod;
  private int period;
  private int repetition;
  private String text;
}