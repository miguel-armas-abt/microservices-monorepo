package com.demo.poc.commons.core.restclient;

import com.demo.poc.commons.core.errors.external.ExternalErrorHandlerUtil;
import com.demo.poc.commons.core.errors.external.strategy.RestClientErrorStrategy;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import com.demo.poc.commons.core.properties.restclient.HeaderTemplate;
import com.demo.poc.commons.core.restclient.dto.ExchangeRequestDTO;
import com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

@RequiredArgsConstructor
public class CustomRestTemplate {

  private final ConfigurationBaseProperties properties;
  private final List<RestClientErrorStrategy> errorStrategies;
  private final List<ClientHttpRequestInterceptor> interceptors;

  public <I, O> O exchange(ExchangeRequestDTO<I, O> request, String serviceName) {
    try {
      return RestTemplateFactory
          .createRestTemplate(interceptors)
          .exchange(request.getUrl(),
              request.getHttpMethod(),
              createHttpEntity(request, properties.getRestClients().get(serviceName).getRequest().getHeaders()),
              request.getResponseClass(),
              request.getUriVariables())
          .getBody();

    } catch (HttpStatusCodeException httpException) {
      throw ExternalErrorHandlerUtil.build(httpException, request.getErrorWrapperClass(), serviceName, errorStrategies, properties);
    }
  }

  private static <I, O> HttpEntity<I> createHttpEntity(ExchangeRequestDTO<I, O> request,
                                                       HeaderTemplate headerTemplate) {
    HttpHeaders headers = HttpHeadersFiller.generateHeaders(headerTemplate, request.getHeaders());
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(request.getRequestBody(), headers);
  }
}
