package com.siemens.plm.mindsphere.sdi.featuretest;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class FeatureTestApp {

	public static void main(String[] args) {
		SpringApplication.run(FeatureTestApp.class, args);
	}

	@GetMapping("/server/*")
	public String get(HttpServletRequest request) {

		return request.getRequestURI();
	}

}
