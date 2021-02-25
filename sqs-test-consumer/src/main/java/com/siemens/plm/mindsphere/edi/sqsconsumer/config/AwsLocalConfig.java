package com.siemens.plm.mindsphere.edi.sqsconsumer.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@ConditionalOnProperty(
    value = "local-configuration.aws.enabled",
    havingValue = "true"
)
public class AwsLocalConfig {

  private AWSCredentialsProvider credentialsProvider;

  @Value("${aws.region}")
  private String region;

  @Value("${aws.sqs.url}")
  private String localSQS;

  @Autowired
  public AwsLocalConfig() {
    log.info("Using LocalStack");
    this.credentialsProvider =
        new AWSStaticCredentialsProvider(new BasicAWSCredentials("foo", "bar"));
  }


  @Bean
  public AWSCredentialsProvider awsCredentialsProvider() {
    log.info("USing AnonymousAWSCredentials");
    return this.credentialsProvider;
  }


  @Bean
  public AmazonSQS amazonSQS() {
    return AmazonSQSAsyncClientBuilder.standard()
        .withCredentials(this.credentialsProvider)
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(localSQS, this.region))
        .build();
  }

}
