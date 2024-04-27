package com.demo.bbq.config.errors.handler;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.external.RestClientErrorService;
import com.demo.bbq.utils.errors.handler.ResponseErrorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseErrorHandler extends ResponseEntityExceptionHandler {

  private final ServiceConfigurationProperties properties;
  private final  List<RestClientErrorService> errorServices;

  @ExceptionHandler({Throwable.class})
  public final ResponseEntity<ErrorDTO> handleException(Throwable exception, WebRequest request) {
    return ResponseErrorUtil.handleException(properties, errorServices, exception, request);
  }
}