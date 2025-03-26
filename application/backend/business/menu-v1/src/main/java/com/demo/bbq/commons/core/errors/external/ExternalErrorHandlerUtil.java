package com.demo.bbq.commons.core.errors.external;

import com.demo.bbq.commons.core.errors.dto.ErrorDTO;
import com.demo.bbq.commons.core.errors.dto.ErrorType;
import com.demo.bbq.commons.core.errors.exceptions.ExternalServiceException;
import com.demo.bbq.commons.core.errors.exceptions.NoSuchStrategyException;
import com.demo.bbq.commons.core.errors.external.strategy.ExternalErrorWrapper;
import com.demo.bbq.commons.core.errors.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.core.properties.ConfigurationBaseProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

import static com.demo.bbq.commons.core.errors.external.ExternalErrorSelector.getDefaultResponse;
import static com.demo.bbq.commons.core.errors.external.ExternalErrorSelector.selectCode;
import static com.demo.bbq.commons.core.errors.external.ExternalErrorSelector.selectHttpCode;
import static com.demo.bbq.commons.core.errors.external.ExternalErrorSelector.selectMessage;
import static com.demo.bbq.commons.core.errors.external.ExternalErrorSelector.selectType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExternalErrorHandlerUtil {

  public static ExternalServiceException build(HttpStatusCodeException httpException,
                                               Class<? extends ExternalErrorWrapper> errorWrapperClass,
                                               String serviceName,
                                               List<RestClientErrorStrategy> serviceList,
                                               ConfigurationBaseProperties properties) {
    ErrorDTO defaultError = ErrorDTO.getDefaultError(properties);

    String jsonBody = httpException.getResponseBodyAsString();

    Pair<String, String> codeAndMessage = Strings.EMPTY.equals(jsonBody)
        ? getDefaultResponse(defaultError, "Empty response")
        : selectStrategy(errorWrapperClass, serviceList).getCodeAndMessage(jsonBody).orElseGet(() -> getDefaultResponse(defaultError, "No such external error wrapper"));

    String selectedCode = selectCode(properties, codeAndMessage.getLeft(), serviceName);
    String selectedMessage = selectMessage(properties, selectedCode, codeAndMessage.getRight(), serviceName);
    ErrorType selectedErrorType = selectType(errorWrapperClass);
    HttpStatusCode selectedHttpStatus = HttpStatusCode.valueOf(selectHttpCode(properties, httpException.getStatusCode().value(), errorWrapperClass, codeAndMessage.getLeft(), serviceName));

    return new ExternalServiceException(selectedCode, selectedMessage, selectedErrorType, selectedHttpStatus);
  }

  private static RestClientErrorStrategy selectStrategy(Class<? extends ExternalErrorWrapper> errorWrapperClass,
                                                        List<RestClientErrorStrategy> serviceList) {
    return serviceList
        .stream()
        .filter(service -> service.supports(errorWrapperClass))
        .findFirst()
        .orElseThrow(NoSuchStrategyException::new);
  }
}
