package com.demo.poc.commons.errors.handler.response;

import static com.demo.poc.commons.errors.dto.ErrorDTO.CODE_DEFAULT;

import com.demo.poc.commons.errors.dto.ErrorDTO;
import com.demo.poc.commons.errors.dto.ErrorType;
import com.demo.poc.commons.errors.exceptions.AuthorizationException;
import com.demo.poc.commons.errors.exceptions.BusinessException;
import com.demo.poc.commons.errors.exceptions.SystemException;
import com.demo.poc.commons.properties.ConfigurationBaseProperties;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class ResponseErrorHandlerBaseUtil {

  public static <T extends Throwable> ErrorDTO build(ConfigurationBaseProperties properties, T exception) {
    ErrorDTO currentError = getError(exception);
    String defaultMessage = ErrorDTO.getDefaultError(properties).getMessage();
    String errorCode = Optional.of(currentError.getCode()).orElseGet(() -> CODE_DEFAULT);
    String matchingMessage = ErrorDTO.getMatchMessage(properties, errorCode);
    boolean showCustomMessages = properties.errorMessages().get().enabled();

    String selectedMessage = selectMessage(showCustomMessages, defaultMessage, currentError.getMessage(), matchingMessage);
    currentError.setMessage(selectedMessage);
    return currentError;
  }

  private static String selectMessage(boolean showCustomMessages, String defaultMessage,
                                      String currentMessage, String matchingMessage) {
    return showCustomMessages
        ? Optional.ofNullable(matchingMessage).orElse(defaultMessage)
        : Optional.ofNullable(currentMessage).orElse(defaultMessage);
  }

  private static <T extends Throwable> ErrorDTO getError(T exception) {
    ErrorDTO error = null;

    if (exception instanceof BusinessException businessException)
      error = businessException.getErrorDetail();

    if (exception instanceof SystemException systemException)
      error = systemException.getErrorDetail();

    if (exception instanceof AuthorizationException systemException)
      error = systemException.getErrorDetail();

    if(exception instanceof ConstraintViolationException constraintViolationException)
      error = getError(constraintViolationException);

    return error;
  }

  private static ErrorDTO getError(ConstraintViolationException constraintViolationException) {
    String message = constraintViolationException
        .getConstraintViolations()
        .stream()
        .map(ConstraintViolation::getMessage)
        .reduce((errorMessage, newErrorMessage) -> StringUtils.EMPTY.equals(newErrorMessage) ? errorMessage : errorMessage + ", " + newErrorMessage)
        .orElseGet(() -> CODE_DEFAULT);

    return ErrorDTO.builder()
        .code(CODE_DEFAULT)
        .message(message)
        .type(ErrorType.BUSINESS)
        .build();
  }
}
