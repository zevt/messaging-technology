package com.siemens.plm.mindsphere.edi.snsproducer.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.google.common.base.Strings;
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
    havingValue = "false")
public class AwsConfig {


  private AWSCredentialsProvider credentialsProvider;
  private String region;

  @Autowired
  public AwsConfig(
      @Value("${aws.credentials.accessKey:#{null}}") String accessKey,
      @Value("${aws.credentials.secretKey:#{null}}") String secretKey,
      @Value("${aws.region}") String region) {

    if (Strings.isNullOrEmpty(accessKey) || Strings.isNullOrEmpty(secretKey)) {
      log.info("Using InstanceProfileCredentialsProvider");
      this.credentialsProvider = new InstanceProfileCredentialsProvider(false);
    } else {
      log.info("Using AWSStaticCredentialsProvider");
      this.credentialsProvider =
          new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
    }
    this.region = region;
  }

  @Bean
  public AWSCredentialsProvider awsCredentialsProvider() {
    return this.credentialsProvider;
  }

  @Bean
  public AmazonSNS amazonSNS() {
    return AmazonSNSClientBuilder
        .standard()
        .withRegion(this.region)
        .withCredentials(this.credentialsProvider)
        .build();
  }
}
