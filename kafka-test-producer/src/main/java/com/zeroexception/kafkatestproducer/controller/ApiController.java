package com.zeroexception.kafkatestproducer.controller;

import com.zeroexception.kafkatestproducer.model.TestConfig;
import com.zeroexception.kafkatestproducer.service.KafkaTestService;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

  private KafkaTestService testService;

  @Autowired
  public ApiController(
      KafkaTestService testService) {
    this.testService = testService;
  }

  @PostMapping("/trigger")
  public ResponseEntity<?> triggerKafkaMessages(@RequestBody TestConfig testConfig) {
    testConfig.setTestId(Instant.now().toString());
    this.testService.runTest(testConfig);
    return new ResponseEntity<>("Test has triggered. Check DB", HttpStatus.ACCEPTED);
  }

  @PostMapping("/test-consumer")
  public ResponseEntity<?> experiment(@RequestBody TestConfig testConfig) {
    testConfig.setTestId(Instant.now().toString());
    this.testService.runTest(testConfig, "kafka-test");
    return new ResponseEntity<>("Test has triggered. Check DB", HttpStatus.ACCEPTED);
  }

}
