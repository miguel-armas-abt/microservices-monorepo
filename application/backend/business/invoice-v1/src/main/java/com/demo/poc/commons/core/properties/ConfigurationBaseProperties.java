package com.demo.poc.commons.core.properties;

import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientException;
import com.demo.poc.commons.core.logging.enums.LoggingType;
import com.demo.poc.commons.core.properties.cache.CacheTemplate;
import com.demo.poc.commons.core.properties.obfuscation.ObfuscationTemplate;
import com.demo.poc.commons.core.properties.restclient.HeaderTemplate;
import com.demo.poc.commons.core.properties.restclient.PerformanceTemplate;
import com.demo.poc.commons.core.properties.restclient.RestClient;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public abstract class ConfigurationBaseProperties {

  protected ProjectType projectType;

  protected Map<String, Boolean> enabledLoggers;

  protected Map<String, String> errorMessages;

  protected Map<String, RestClient> restClients;

  protected Map<String, CacheTemplate> cache;

  protected ObfuscationTemplate obfuscation;

  public PerformanceTemplate searchPerformance(String serviceName) {
    return searchRestClient(serviceName).getPerformance();
  }

  public String searchEndpoint(String serviceName) {
    return searchRestClient(serviceName).getRequest().getEndpoint();
  }

  public Map<String, String> searchFormData(String serviceName) {
    return searchRestClient(serviceName).getRequest().getFormData();
  }

  public HeaderTemplate searchHeaders(String serviceName) {
    return searchRestClient(serviceName).getRequest().getHeaders();
  }

  private RestClient searchRestClient(String serviceName) {
    return Optional.ofNullable(restClients.get(serviceName))
        .orElseThrow(NoSuchRestClientException::new);
  }

  public boolean isLoggerPresent(LoggingType loggingType) {
    return Optional.ofNullable(this.getEnabledLoggers())
        .filter(enabledLoggers -> enabledLoggers.containsKey(loggingType.getCode()))
        .map(enabledLoggers -> enabledLoggers.get(loggingType.getCode()))
        .orElseGet(() -> Boolean.FALSE);
  }
}