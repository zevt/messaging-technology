package com.zeroexception.commons.messaging.consumer.task;

public interface Task<R> extends Runnable {
  String getId();
  void onFailure(Throwable throwable);
  void onSuccess(R result);
  void onSuccess();
  R getResult();
}
