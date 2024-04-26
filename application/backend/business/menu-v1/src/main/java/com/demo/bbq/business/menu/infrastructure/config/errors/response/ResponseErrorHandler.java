package com.demo.bbq.business.menu.infrastructure.config.errors.response;

import com.demo.bbq.business.menu.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import com.demo.bbq.utils.errors.exceptions.ExternalServiceException;
import com.demo.bbq.utils.errors.exceptions.SystemException;
import com.demo.bbq.utils.errors.handler.response.ResponseErrorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseErrorHandler extends ResponseEntityExceptionHandler {

  private final ServiceConfigurationProperties properties;

  @ExceptionHandler({SystemException.class, BusinessException.class, ExternalServiceException.class})
  public final ResponseEntity<ErrorDTO> handleException(Throwable exception, WebRequest request) {
    return ResponseErrorUtil.handleException(properties, exception, request);
  }
}