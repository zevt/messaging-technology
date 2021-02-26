package com.zeroexception.commons.messaging.test.service;


import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


@Slf4j
public class ScheduledService {

  private static TaskScheduler TASK_SCHEDULER;

  static {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(Runtime.getRuntime().availableProcessors() * 4);
    log.info("Pool size = {}", scheduler.getPoolSize());
    scheduler.initialize();
    TASK_SCHEDULER = scheduler;
  }

  /**
   *
   * @param task function which will execute every period of time
   * @param period in seconds
   * @param totalExecution total execution of task
   */
  public void schedule(Runnable task, long period, long totalExecution) {
    log.info("Schedule Job with period of {} seconds and totalExecution of {} times", period, totalExecution);
    ScheduledFuture scheduledFuture = TASK_SCHEDULER.scheduleAtFixedRate(task, Duration.ofSeconds(period));
    TASK_SCHEDULER.schedule(
        () -> scheduledFuture.cancel(false),
        Date.from(Instant.now().plusMillis(period  * totalExecution * 1000 - 500)));
  }
}
