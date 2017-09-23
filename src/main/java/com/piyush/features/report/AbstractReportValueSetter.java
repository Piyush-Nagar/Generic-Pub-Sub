package com.piyush.features.report;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Piyush on 8/4/17.
 */
public abstract class AbstractReportValueSetter<I> {

  @Value("${client.code}")
  private String clientCode;

  public abstract boolean setValue(I i, String id);


  public String getClientCode() {
    return clientCode;
  }
}
