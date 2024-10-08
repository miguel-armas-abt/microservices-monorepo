package com.demo.bbq.commons.properties.dto.restclient;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestClient {

  private PerformanceTemplate performance;
  private RequestTemplate request;
  private Map<String, RestClientError> errors;

}
