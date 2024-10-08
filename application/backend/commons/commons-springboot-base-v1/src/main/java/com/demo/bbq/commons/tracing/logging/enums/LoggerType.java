package com.demo.bbq.commons.tracing.logging.enums;

import java.util.Arrays;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoggerType {

  ERROR("error", "Error"),
  REST_SERVER_REQ("rest.server.req", "REST server request"),
  REST_SERVER_RES("rest.server.res", "REST server response"),
  REST_CLIENT_REQ("rest.client.req", "REST client request"),
  REST_CLIENT_RES("rest.client.res", "REST client response");

  private final String code;
  private final String message;

  public LoggerType findByCode(String code) {
    return Arrays.stream(values())
        .filter(loggerType -> loggerType.getCode().equals(code))
        .findFirst()
        .orElseThrow(() -> new SystemException("NoSuchLoggerType"));
  }
}