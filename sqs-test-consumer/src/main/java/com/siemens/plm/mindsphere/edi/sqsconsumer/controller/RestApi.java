package com.siemens.plm.mindsphere.edi.sqsconsumer.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RestApi {

  @Autowired
  private AmazonSQS amazonSQS;

  @Value("${aws.sqs.queue}")
  private String queue;
  @GetMapping("/messages")
  public ResponseEntity<?> getMessages() {

    GetQueueUrlResult queryUrl =  amazonSQS.getQueueUrl(queue);
//    SetQueueAttributesRequest setQueueAttributesRequest = new SetQueueAttributesRequest()
//        .withQueueUrl(queryUrl.getQueueUrl())
//        .addAttributesEntry("ReceiveMessageWaitTimeSeconds", "20");
//    amazonSQS.setQueueAttributes(setQueueAttributesRequest);

    ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queryUrl.getQueueUrl());
    receiveMessageRequest.setMaxNumberOfMessages(2);
    List<Message> messageList = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
    messageList.forEach( m -> {
      log.info("Message: {}", m.getBody());
      amazonSQS.deleteMessage(queryUrl.getQueueUrl(), m.getReceiptHandle());
    });

    return new ResponseEntity<>("", HttpStatus.OK);
  }
}
