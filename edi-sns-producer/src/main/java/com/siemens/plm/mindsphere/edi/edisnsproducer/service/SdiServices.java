package com.siemens.plm.mindsphere.edi.edisnsproducer.service;


import com.amazonaws.services.sns.model.PublishResult;
import com.siemens.plm.mindsphere.edi.messaging.model.EdiMessage;
import com.siemens.plm.mindsphere.edi.messaging.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SdiServices {

  @Value("${aws.sns.topic.data-ingest-file-converter}")
  private String dataInjectFileConverter;

  @Autowired
  private MessageProducer<EdiMessage, PublishResult> messageProducer;



}
