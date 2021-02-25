package com.siemens.plm.mindsphere.edi.sqsconsumer.task;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.siemens.plm.mindsphere.edi.messaging.aws.util.AWSMessageConverter;
import com.siemens.plm.mindsphere.edi.messaging.aws.util.SQSUtils;
import com.siemens.plm.mindsphere.edi.messaging.consumer.Task;
import com.siemens.plm.mindsphere.edi.messaging.consumer.TaskFactory;
import com.siemens.plm.mindsphere.edi.sqsconsumer.aws.SQSMessageConverter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SqsTaskFactory implements TaskFactory<Message> {

  public SqsTaskFactory(AmazonSQS amazonSQS, String queue) {
    this.amazonSQS = amazonSQS;
    this.queue = queue;
  }

  private AmazonSQS amazonSQS;
  private String queue;

  @Override
  public Task<Message> create(Message message) {
    return new TaskImpl(amazonSQS, queue, message);
  }

  class TaskImpl implements Task<Message> {

    private AmazonSQS amazonSQS;
    private String queue;
    private Message message;
    private Map<String, String> attributes;

    public TaskImpl(AmazonSQS amazonSQS, String queue, Message message) {
      this.amazonSQS = amazonSQS;
      this.queue = queue;
      this.message = message;

      attributes = AWSMessageConverter.extractHeader(message);

      log.info("Attributes size: {}", attributes.size());
      this.attributes.forEach(
          (k, v) -> {
            log.info(k + ": " + v);
          });
    }

    @Override
    public String getId() {
      return "JobId is not implemented yet";
    }

    @Override
    public void run() {
//        Assuming the function run take 1400 millis seconds to run
      try {
        Object m = new SQSMessageConverter().extractPayload(message, Object.class);
        log.info("Task for message: {}", m);
        Thread.sleep(1500);
        log.info("DONE");
      } catch (InterruptedException e) {
//          Handle failure related to business logic
        e.printStackTrace();
      }
    }

    @Override
    public void onFailure(Throwable throwable) {
      log.error(" Failed to process massage: {}", throwable.getMessage());
      SQSUtils.deleteMessage(amazonSQS, queue, message);
    }

    @Override
    public void onSuccess() {
      log.info("Handle Success here: ");
      SQSUtils.deleteMessage(amazonSQS, queue, message);
    }

  }

}
