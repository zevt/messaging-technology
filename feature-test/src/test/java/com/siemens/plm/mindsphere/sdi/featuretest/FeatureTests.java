package com.siemens.plm.mindsphere.sdi.featuretest;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources",
//    glue = "com.siemens.plm.mindsphere.sdi.featuretest.ServerAppTests",
//    tags = {"@RestApi"},
    plugin = {"pretty", "json:target/cucumber-report.json"}
)
public class FeatureTests {

}
