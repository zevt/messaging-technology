package com.zeroexception.commons.messaging.consumer.task;

public interface TaskFactory<T, R> {

  Task<R> create(T message);
}
