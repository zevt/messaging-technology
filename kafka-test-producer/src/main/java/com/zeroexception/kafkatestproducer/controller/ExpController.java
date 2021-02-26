package com.zeroexception.kafkatestproducer.controller;

import java.util.concurrent.Executors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.SettableListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exp")
public class ExpController {

  @GetMapping("/listener")
  public ResponseEntity<?> testListenable() {

    SettableListenableFuture<String> future = new SettableListenableFuture<>();

    Runnable r = () -> {
      try {
        Thread.sleep(1500);
        future.set("OK I'm Done");
      } catch (InterruptedException e) {
        future.setException(e);
      }};


    Executors.newSingleThreadExecutor().submit(r);
    future.addCallback( System.out::println, e -> e.printStackTrace());

    System.out.println(" After I'm done ");

    return new ResponseEntity<>(null, HttpStatus.OK);
  }
}
