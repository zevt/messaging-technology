package com.zeroexception.kafkatestconsumer.task;

import com.amazonaws.services.sqs.model.Message;
import com.zeroexception.commons.messaging.consumer.task.Task;
import com.zeroexception.commons.messaging.consumer.task.TaskFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerTaskFactory implements TaskFactory<Message, Object> {



  @Override
  public Task create(Message message) {

    return new Task() {

      @Override
      public void run() {
//        Assuming the function run take 1500 millis seconds to run
        try {
          log.info("Task for message: {}", message);
          Thread.sleep(1500);
          log.info("DONE");
        } catch (InterruptedException e) {
//          Handle failure related to business logic
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(Throwable throwable) {
        throw new UnsupportedOperationException("Not Implemented Method");

      }

      @Override
      public void onSuccess(Object result) {
        log.info("Handle Success here: ");
//        log.info("Delete Message from the queue");

      }

      @Override
      public Object getResult() {
        return null;
      }

      @Override
      public String getId() {
        return null;
      }

      @Override
      public void onSuccess() {

      }
    };
  }

}
