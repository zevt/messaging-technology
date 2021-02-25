package com.siemens.plm.mindsphere.edi.commons.messaging.consumer.task;

public interface TaskFactory<T, R> {

  Task<R> create(T message);
}
