package com.demo.bbq.commons.tracing.logging.constants;

import static com.demo.bbq.commons.tracing.logging.constants.LoggingMessage.BASE_EXCEPTION_MESSAGES;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.TimeoutException;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionLoggingMessage {

  public static Map<Class<? extends Throwable>, String> getExceptionMessages() {
    Map<Class<? extends Throwable>, String> exceptionMessages = new HashMap<>(BASE_EXCEPTION_MESSAGES);
    exceptionMessages.put(TimeoutException.class, "Timeout occurred while processing request");
    exceptionMessages.put(ConnectTimeoutException.class, "Connection timeout while attempting to connect to the server");
    exceptionMessages.put(ReadTimeoutException.class, "Read timeout occurred while waiting for the server's response");
    return exceptionMessages;
  }

}
