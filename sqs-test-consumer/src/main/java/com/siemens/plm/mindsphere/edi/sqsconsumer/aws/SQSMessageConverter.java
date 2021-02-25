package com.siemens.plm.mindsphere.edi.sqsconsumer.aws;


import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

/**
 * This class convert Message defined by AWS SQS model to specified type.
 */
public class SQSMessageConverter {

  /**
   * Function extracting payload from AWS SQS Message
   * @param message Message defined by AWS SQS model
   * @param type class type of returned payload
   * @param <T> generic type of returned payload
   * @exception NullPointerException when message is null or message does not contains proper payload.
   * @exception JsonParseException when error happened while converting to payload. The exception might be subtype of JsonParseException, which are JsonSyntaxException or JsonIOException
   * @return payload
   */
  public<T> T extractPayload(Message message, Class<T> type) {
    Gson gson = new Gson();
    TypeReference typeRef = new TypeReference<HashMap<String, String>>() {};
    Map<String, String> payload = gson.fromJson(message.getBody(), typeRef.getType());
    return gson.fromJson(payload.get("Message"), type);
  }
}
