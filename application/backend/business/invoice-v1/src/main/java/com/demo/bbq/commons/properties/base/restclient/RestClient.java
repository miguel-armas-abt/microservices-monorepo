package com.demo.bbq.commons.properties.base.restclient;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class RestClient {

  private PerformanceTemplate performance;
  private RequestTemplate request;
  private Map<String, RestClientError> errors;

}
