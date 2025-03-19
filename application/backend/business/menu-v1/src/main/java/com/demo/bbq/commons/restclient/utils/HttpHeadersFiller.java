package com.demo.bbq.commons.restclient.utils;

import com.demo.bbq.commons.properties.base.restclient.HeaderTemplate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpHeadersFiller {

  public static HttpHeaders generateHeaders(HeaderTemplate headerTemplate, Map<String, String> currentHeaders) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAll(HeadersFiller.fillHeaders(headerTemplate, currentHeaders));
    return httpHeaders;
  }
}