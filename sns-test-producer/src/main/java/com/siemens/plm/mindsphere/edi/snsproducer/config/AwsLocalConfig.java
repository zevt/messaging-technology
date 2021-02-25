package com.siemens.plm.mindsphere.edi.snsproducer.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
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

  @Value("${aws.sns.url}")
  private String localSNS;

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
            new AwsClientBuilder.EndpointConfiguration(localSNS, this.region))
        .build();
  }
}
