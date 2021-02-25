package com.siemens.plm.mindsphere.edi.edisnsproducer.config;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.siemens.plm.mindsphere.edi.messaging.aws.producer.SnsMessageProducer;
import com.siemens.plm.mindsphere.edi.messaging.model.EdiMessage;
import com.siemens.plm.mindsphere.edi.messaging.producer.MessageProducer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MessagingConfig {

  @Value("${aws.sns.topic.data-ingest-file-converter}")
  private String dataInjectFileConverter;

  @Value("${aws.sns.topic.file-converter-schema-extractor}")
  private String fileConverterSchemaExt;


  @Value("${aws.sns.topic.jobStatus}")
  private String jobStatus;

  @Bean(name = "DataInjectFileConverter")
  public String dataInjectFileConverter() {
    return dataInjectFileConverter;
  }

  @Bean(name = "FileConverterSchemaExt")
  public String fileConverterSchemaExt() {
    return fileConverterSchemaExt;
  }

  @Bean(name = "JobStatus")
  public String jobStatus() {
    return jobStatus;
  }

  @Autowired
  private AmazonSNS amazonSNS;

  @Bean(name = "MessageProducer")
  public MessageProducer<EdiMessage, PublishResult> messageProducer() {
    return new SnsMessageProducer(jobStatus, amazonSNS);
  }

}
