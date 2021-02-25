package com.siemens.plm.mindsphere.edi.snsproducer.model;

import lombok.Data;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.TypeAlias;
//import org.springframework.data.mongodb.core.mapping.Document;

//@Document
//@TypeAlias("InstanceConfig")
@Data
public class InstanceConfig {
//  @Id
  private String id;
  private String type;

  public InstanceConfig() {
    this.type = "Producer Instance";
  }
}
