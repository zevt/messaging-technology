package com.siemens.plm.mindsphere.sdi.featuretest;


import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Ignore
@Slf4j
public class ServerAppTests {

  protected final String SERVER_URL = "http://localhost";

  @LocalServerPort
  protected int port;

  @Test
  public void contextLoads() {
    String url = this.SERVER_URL + ":" + port + "/actuator/health";
    log.info("url {}", url);
    RestTemplate rest = new RestTemplate();
    ObjectNode response = rest.getForObject(url, ObjectNode.class);
    Assert.assertEquals(response.get("status").asText(),"UP");
  }

//  public static String getBaseUrl() {
//    return SERVER_URL + "";
//  }

}
