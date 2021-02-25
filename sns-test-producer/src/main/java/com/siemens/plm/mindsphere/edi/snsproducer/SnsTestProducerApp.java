package com.siemens.plm.mindsphere.edi.snsproducer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnsTestProducerApp implements CommandLineRunner {


  public static void main(String[] args) {
    SpringApplication.run(SnsTestProducerApp.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
//    Map<String, String> env = System.getenv();
//    for (String envName : env.keySet()) {
//      if (envName.contains("AWS")) {
//        System.out.println(envName);
//        System.out.println(env.get(envName));
//      }
//    }
  }
}
