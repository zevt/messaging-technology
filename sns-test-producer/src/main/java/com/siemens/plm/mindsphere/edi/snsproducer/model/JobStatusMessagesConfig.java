package com.siemens.plm.mindsphere.edi.snsproducer.model;

import com.siemens.plm.mindsphere.edi.messaging.model.jobstatus.JobStatusMessage;
import java.util.List;
import lombok.Data;

@Data
public class JobStatusMessagesConfig {
  private List<JobStatusMessage> messages;
}