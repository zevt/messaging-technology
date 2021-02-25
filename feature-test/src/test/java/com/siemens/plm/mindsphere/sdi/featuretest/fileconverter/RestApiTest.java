package com.siemens.plm.mindsphere.sdi.featuretest.fileconverter;

import com.siemens.plm.mindsphere.sdi.featuretest.ServerAppTests;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class RestApiTest extends ServerAppTests {

  private RestTemplate rest = new RestTemplate();
  private String response;

  @When("A Request to Server")
  public void requestToServer() {
    String url = this.SERVER_URL + ":" + port + "/server/abcd";
    log.info("url {}", url);
    response = rest.getForObject(url, String.class);
  }

  @Then("Server return the request url")
  public void verifyResponse() {
    Assert.assertEquals(response, "/server/abcd");
  }





}
