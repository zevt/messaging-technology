package com.siemens.plm.mindsphere.sdi.featuretest.fileconverter;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;


@Slf4j
public class AcceptDataIngestMessage {

  @LocalServerPort
  protected int port;

  @Given("Preparation")
  public void dosomething() {
    log.info("Preparation...");
  }

  @And("meets opportunity")
  public void opp() {
    log.info("opportunity");
  }

  @Then("Luck will be created")
  public void createLuck() {
    log.info("Luck");
    String url = String.format("http://localhost:%s/server/xxxx", this.port);
    String res = new RestTemplate().getForObject(url, String.class);
    Assert.assertEquals(res, "/server/xxxx");

    log.info("Port: {}", this.port);
  }

}
