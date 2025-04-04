package com.demo.poc.commons.core.interceptor.restserver;

import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import com.demo.poc.commons.core.logging.enums.LoggingType;
import com.demo.poc.commons.core.logging.ThreadContextInjector;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.demo.poc.commons.core.logging.enums.LoggingType.REST_SERVER_REQ;
import static com.demo.poc.commons.core.interceptor.restserver.RestServerInterceptor.RequestLoggerUtil.extractRequestBody;
import static com.demo.poc.commons.core.interceptor.restserver.RestServerInterceptor.RequestLoggerUtil.extractRequestHeadersAsMap;
import static com.demo.poc.commons.core.interceptor.restserver.RestServerInterceptor.RequestLoggerUtil.extractRequestURL;
import static com.demo.poc.commons.core.interceptor.restserver.RestServerInterceptor.ResponseLoggerUtil.extractResponseHeadersAsMap;

@Slf4j
@WebFilter(urlPatterns = "/*")
@RequiredArgsConstructor
public class RestServerInterceptor implements Filter {

  private final ThreadContextInjector threadContextInjector;
  private final ConfigurationBaseProperties properties;

  @Override
  public void init(FilterConfig filterConfig) {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;

    BufferingHttpServletResponse bufferingResponse = new BufferingHttpServletResponse((HttpServletResponse) response);
    BufferingHttpServletRequest bufferingRequest = new BufferingHttpServletRequest(httpRequest);

    generateTraceOfRequest(bufferingRequest);

    chain.doFilter(bufferingRequest, bufferingResponse);
    String responseBody = bufferingResponse.getCachedBody();

    generateTraceOfResponse(bufferingResponse, responseBody);

    response.getOutputStream().write(responseBody.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public void destroy() {
  }

  private void generateTraceOfRequest(HttpServletRequest request) {
    if (properties.isLoggerPresent(REST_SERVER_REQ)) {
      threadContextInjector.populateFromRestServerRequest(
          request.getMethod(),
          extractRequestURL(request),
          extractRequestHeadersAsMap(request),
          extractRequestBody(request));
    }
  }

  private void generateTraceOfResponse(HttpServletResponse response, String responseBody) {
    if (properties.isLoggerPresent(LoggingType.REST_SERVER_RES)) {
      threadContextInjector.populateFromRestServerResponse(
          extractResponseHeadersAsMap(response),
          responseBody,
          String.valueOf(response.getStatus()));
    }
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class RequestLoggerUtil {
    public static Map<String, String> extractRequestHeadersAsMap(HttpServletRequest request) {
      return Optional.ofNullable(request.getHeaderNames())
          .map(Collections::list)
          .orElseGet(ArrayList::new)
          .stream()
          .collect(Collectors.toMap(headerName -> headerName, request::getHeader));
    }

    public static String extractRequestBody(HttpServletRequest request) {
      try {
        return new BufferingHttpServletRequest(request).getRequestBody();
      } catch (IOException exception) {
        log.error("Error reading request body: {}", exception.getClass(), exception);
        return StringUtils.EMPTY;
      }
    }

    public static String extractRequestURL(HttpServletRequest request) {
      String url = Optional.of(request)
          .map(HttpServletRequest::getRequestURL)
          .map(StringBuffer::toString)
          .orElse(StringUtils.EMPTY);

      String queryString = request.getQueryString();
      if (StringUtils.isNotBlank(queryString))
        url += "?" + queryString;

      return url;
    }
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ResponseLoggerUtil {
    public static Map<String, String> extractResponseHeadersAsMap(HttpServletResponse response) {
      Map<String, String> headers = new HashMap<>();
      Collection<String> headerNames = response.getHeaderNames();
      headerNames.forEach(headerName -> headers.put(headerName, response.getHeader(headerName)));
      return headers;
    }
  }
}
