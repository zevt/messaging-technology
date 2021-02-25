package com.siemens.plm.mindsphere.edi.sqsconsumer.consumer.jobstatus;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.siemens.plm.mindsphere.edi.messaging.aws.util.AWSMessageConverter;
import com.siemens.plm.mindsphere.edi.messaging.aws.util.SQSUtils;
import com.siemens.plm.mindsphere.edi.messaging.consumer.Task;
import com.siemens.plm.mindsphere.edi.messaging.consumer.TaskFactory;
import com.siemens.plm.mindsphere.edi.messaging.model.jobstatus.JobStatusMessage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("JobStatusTaskFactory")
@Primary
@Slf4j
public class JobStatusTaskFactory implements TaskFactory<Message> {

  @Autowired
  private AmazonSQS amazonSQS;

  @Value("${aws.sqs.queue.job-status}")
  private String queue;

  @Override
  public Task<Message> create(Message message) {
    return new JobStatusTask(message);
  }

  public List<Message> receiveMessages(int size) {
    return SQSUtils.receiveMessage(amazonSQS, queue, size);
  }
  class JobStatusTask implements Task<Message> {
    private Message message;
    private JobStatusMessage jobStatusMessage;

    JobStatusTask(Message message) {
      this.message = message;
      this.jobStatusMessage = AWSMessageConverter.extractPayload(message, JobStatusMessage.class);
    }

    @Override
    public String getId() {
      return jobStatusMessage.getJobId();
    }

    @Override
    public void onFailure(Throwable throwable) {
      log.info("Failure. Now delete message");
      SQSUtils.deleteMessage(amazonSQS, queue, message);
    }

    @Override
    public void onSuccess() {
      log.info("Success. Now delete message");
      SQSUtils.deleteMessage(amazonSQS, queue, message);
    }

    @Override
    public void run() {
      log.info("JobStatus Message: {}", jobStatusMessage);
    }
  }
}
