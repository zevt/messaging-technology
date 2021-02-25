package com.siemens.plm.mindsphere.edi.snsproducer.controller;


import com.siemens.plm.mindsphere.edi.snsproducer.model.JobStatusMessagesConfig;
import com.siemens.plm.mindsphere.edi.snsproducer.model.OfferingMessage;
import com.siemens.plm.mindsphere.edi.snsproducer.model.TestConfig;
import com.siemens.plm.mindsphere.edi.snsproducer.service.CustomMessenger;
import com.siemens.plm.mindsphere.edi.snsproducer.service.TestService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SnsController {

  @Autowired
  private TestService testService;
  @Autowired
  private CustomMessenger customMessenger;


  @PostMapping("/trigger")
  public ResponseEntity<?> triggerKafkaMessages(@RequestBody TestConfig testConfig) {
    this.testService.send(testConfig);
    return new ResponseEntity<>("Test has triggered.", HttpStatus.ACCEPTED);
  }

  @PostMapping("/jobStatus")
  public ResponseEntity<?> sendJobStatus(@RequestBody JobStatusMessagesConfig config) {
    this.testService.send(config);
    return new ResponseEntity<>("Test has triggered.", HttpStatus.ACCEPTED);
  }

  @PostMapping("/offering")
  public ResponseEntity<?> sendOfferingMessage(
      @RequestHeader Map<String, String> headers,
      @RequestBody OfferingMessage message) {
    Map<String, String> filteredHeaders = new HashMap<>();
//    headers.put("offeringId", offeringId);
    List<String> keys = Arrays.asList("offeringid", "appname");
    headers.forEach( (k, v) -> {
      if (keys.contains(k)) {
        switch (k) {
          case "offeringid": filteredHeaders.put("offeringId", v);
          break;
          case "appname": filteredHeaders.put("appName", v);
        }
      }
    });
    this.customMessenger.send(message, filteredHeaders);
    return new ResponseEntity<>("Test has triggered.", HttpStatus.ACCEPTED);
  }
}
