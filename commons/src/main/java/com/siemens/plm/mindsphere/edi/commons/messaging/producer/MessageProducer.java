package com.siemens.plm.mindsphere.edi.commons.messaging.producer;


import org.springframework.util.concurrent.ListenableFuture;

public interface MessageProducer<T, R> {

  /**
   * Non Blocking function which send the message of type T and return the send result of type R
   * @param message type T,
   * @param topic String
   * @return ListenableFuture<R> which will be used to handle success for failure in the future
   */
  ListenableFuture<R> send(T message, String topic);

  /**
   * Non Blocking function which send the message of type T to default topic and return the send result of type R
   * @param message type T,
   * @return ListenableFuture<R> which will be used to handle success for failure in the future
   */

  ListenableFuture<R> send(T message);
}
