package com.siemens.plm.mindsphere.edi.edisnsproducer.controller;

import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.siemens.plm.mindsphere.edi.edisnsproducer.service.TestService;
import com.siemens.plm.mindsphere.edi.messaging.model.DataIngestMessage;
import com.siemens.plm.mindsphere.edi.messaging.model.EdiMessage;
import com.siemens.plm.mindsphere.edi.messaging.model.MessageHeaders;
import com.siemens.plm.mindsphere.edi.messaging.model.QueryExecuteMessage;
import com.siemens.plm.mindsphere.edi.messaging.model.jobstatus.JobStatusMessage;
import com.siemens.plm.mindsphere.edi.messaging.producer.MessageProducer;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EdiMessageController {

  private TestService testService;

  @Value("${aws.sns.topic.data-ingest-file-converter}")
  private String dataInjectFileConverter;

  @Value("${aws.sns.topic.queryExecutor}")
  private String queryExecutorTopic;

  @Autowired
  private MessageProducer<EdiMessage, PublishResult> messageProducer;



  public EdiMessageController(
      TestService testService) {
    this.testService = testService;
  }

  @PostMapping("/file-converter")
  public ResponseEntity<?> triggerFileConverter(@RequestBody DataIngestMessage message) {

    message.setJobId("jobId_" + Instant.now().toEpochMilli());
    message.setStartedAt(Instant.now().toEpochMilli());
    ListenableFuture<PublishResult> future = this.messageProducer.send(message, dataInjectFileConverter);
    future.addCallback(publishResult -> {
          log.info(
              "Pushed message to {} successfully",
              dataInjectFileConverter);
        },
        e ->
            log.error(
                "[JobId: {}] Failed to push message back to failure queue",
                message.getJobId()));
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put("jobId", message.getJobId());
    return ResponseEntity.ok(node);
  }


  @PostMapping("/query-executor")
  public ResponseEntity<?> queryExecutor(
      HttpServletRequest request,
      @RequestBody QueryExecuteMessage message) {

    Map<String, String> headers = new HashMap<>();
    String queryType = request.getHeader("QUERY_TYPE");
    if (queryType != null)
      headers.put("QUERY_TYPE", queryType);
    headers.put(MessageHeaders.TRACE_ID, "BUSINESS_SQL");
    String tenant = "tenant";
    headers.put("tenantId", tenant);

    String physical = request.getHeader("physical");
    if (physical == null)
      message.setExecutionId(Instant.now().getEpochSecond() % 100 + "");

    ListenableFuture<PublishResult> future = this.messageProducer.send(message, headers, this.queryExecutorTopic);

    future.addCallback(publishResult -> {
          log.info(
              "Pushed message to {} successfully",
              dataInjectFileConverter);
        },
        e ->
            log.error(
                "[ExecutionID: {}] Failed to push message back to failure queue",
                message.getExecutionId()));
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put("ExecutionID", message.getExecutionId());
    return ResponseEntity.ok(node);
  }

  @PostMapping("/jobStatus")
  public ResponseEntity<?> postJobStatus(@RequestParam("numberOfMessages") int size,
      @RequestBody JobStatusMessage message) {

    this.testService.publish(message, size);
    return null;
  }
}
