package com.siemens.plm.mindsphere.edi.commons.messaging.consumer.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Sole responsibility of TaskExecutor is to execute tasks in concurrent mode. This TaskExecutor implementation is optimized for consumer service to process messages received from a messaging system.
 * The value of Threads Per Processor Ratio (tppRatio) depends on the nature of the task which the application needs to perform. Value of tppRatio needs to be set properly, not too large to overload CPU over 90% and not too small to utilize CPU below 50%.
 *
 */
@Slf4j
@Getter
public class TaskExecutor {
  //   Threads per processor Ratio
  private final int tppRatio;
  private final int threadPoolSize;
  private final AtomicInteger atomicCounter = new AtomicInteger(0);
  private AtomicReference<CountDownLatch> latchRef;
  private final ExecutorService executorService;

  public TaskExecutor(int tppRatio) {
    this.tppRatio = tppRatio;
    this.threadPoolSize = tppRatio * Runtime.getRuntime().availableProcessors();
    this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    this.latchRef = new AtomicReference<>(new CountDownLatch(threadPoolSize));
  }

  /**
   * Process task in concurrent mode. Call function onSuccess() or onFailure() accordingly
   * @param task: task to be processed
   */
  private void process(Task task) {

    log.info("Current task count: {}", atomicCounter.incrementAndGet());
    Runnable r =
        () -> {
          log.info("Executing taskId {}", task.getId());
          try {
            task.run();
            task.onSuccess();
          } catch (Exception e) {
            task.onFailure(e);
          } finally {
            atomicCounter.decrementAndGet();
            latchRef.get().countDown();
          }
        };
    executorService.submit(r);

  }

  /**
   * This function push task to be processed if there are available thread.
   * In case there is not any available thread then this function will block and wait until there is at least one or 1 hour timeout.
   * @param task task to be pushed
   */
  public void push(Task task) {
    if (getAvailableThreads() == 0)  {
      try {
        latchRef.get().await(1, TimeUnit.HOURS);
        latchRef.set(new CountDownLatch(1));
      } catch (InterruptedException e) {
        log.error("Failed while block pushing task with error {}", e.getMessage());
      }
    }
    log.info("Push taskId {}", task.getId());
    process(task);
  }

  /**
   * Return number of threads which is not running any task
   * @return number of free threads
   */
  public int getAvailableThreads() {
    int n = threadPoolSize - atomicCounter.get();
    return n > 0 ? n : 0;
  }

}
