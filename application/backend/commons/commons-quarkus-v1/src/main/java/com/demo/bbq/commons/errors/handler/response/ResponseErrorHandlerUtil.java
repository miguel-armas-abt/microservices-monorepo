package com.demo.bbq.commons.errors.handler.response;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.exceptions.AuthorizationException;
import com.demo.bbq.commons.errors.exceptions.BusinessException;
import com.demo.bbq.commons.errors.exceptions.ExternalServiceException;
import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.tracing.logging.ErrorLoggingUtil;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResponseErrorHandlerUtil {

  public static Response toResponse(Throwable throwable, ConfigurationBaseProperties properties) {
    ErrorLoggingUtil.generateTrace(throwable);

    ErrorDTO error = ErrorDTO.getDefaultError(properties);
    Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

    if (throwable instanceof WebApplicationException webApplicationException) {
      status = Response.Status.fromStatusCode(webApplicationException.getResponse().getStatus());
    }

    if (throwable instanceof ExternalServiceException externalServiceException) {
      error = externalServiceException.getErrorDetail();
      status = Response.Status.fromStatusCode(((ExternalServiceException) throwable).getHttpStatusCode().getStatusCode());
    }

    if (throwable instanceof BusinessException businessException) {
      error = ResponseErrorHandlerBaseUtil.build(properties, businessException);
      status = Response.Status.BAD_REQUEST;
    }

    if (throwable instanceof SystemException systemException) {
      error = ResponseErrorHandlerBaseUtil.build(properties, systemException);
    }

    if (throwable instanceof AuthorizationException authorizationException) {
      error = ResponseErrorHandlerBaseUtil.build(properties, authorizationException);
      status = Response.Status.UNAUTHORIZED;
    }

    return Response.status(status)
        .entity(error)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
