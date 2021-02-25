package com.siemens.plm.mindsphere.edi.edisnsproducer.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
    value = "local-configuration.aws.enabled",
    havingValue = "true"
)
@Slf4j
public class AwsLocalConfig {

  private AWSCredentialsProvider credentialsProvider;

  @Value("${aws.region}")
  private String region;

  @Value("${aws.localstack}")
  private String localStack;

  @Autowired
  public AwsLocalConfig() {
    log.info("Using LocalStack");
    this.credentialsProvider =
        new AWSStaticCredentialsProvider(new BasicAWSCredentials("foo", "bar"));
  }

  @Bean
  public AmazonSNS amazonSNS() {
    return AmazonSNSClientBuilder.standard()
        .withCredentials(this.credentialsProvider)
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(localStack, this.region))
        .build();
  }


  @Bean
  public AmazonSQS amazonSQS() {
    return AmazonSQSClientBuilder.standard()
        .withCredentials(this.credentialsProvider)
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(localStack, this.region))
        .build();
  }

}
