package com.zeroexception.kafkatestconsumer.expprocessor;

import com.google.common.primitives.Ints;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaTestProcessor {
  private static final int MAX_PROCESSORS = Runtime.getRuntime().availableProcessors();
  private static final AtomicInteger ATOMIC_COUNTER = new AtomicInteger(0);
  private static CountDownLatch latch = new CountDownLatch(MAX_PROCESSORS);

  private void process(String message) {
//    Random random = new Random();
//    random.nextInt(2000) + 2000
    new Thread( () -> {
      ATOMIC_COUNTER.incrementAndGet();
      try {
        log.info(" Total Job under processing {}", ATOMIC_COUNTER.get());
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally{
        latch.countDown();
      }
      ATOMIC_COUNTER.decrementAndGet();
    }).start();

  }

  public void pushMessage(String message) {
    if (ATOMIC_COUNTER.get() >= MAX_PROCESSORS)  {
      try {
       latch.await();
       latch = new CountDownLatch(Ints.min(ATOMIC_COUNTER.get(), 4));
       } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    process(message);
  }
}
