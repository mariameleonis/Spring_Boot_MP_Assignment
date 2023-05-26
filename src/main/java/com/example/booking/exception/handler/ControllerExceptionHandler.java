package com.example.booking.exception.handler;

import com.example.booking.exception.BusinessException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

  public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
  private final Counter counter;

  public ControllerExceptionHandler(MeterRegistry meterRegistry) {
    counter = Counter
        .builder("http.status.500.counter")
        .register(meterRegistry);
  }

  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  @ResponseBody
  public String handleInternalError(Exception e) throws Exception {
    if (e instanceof BusinessException) {
      throw e;
    }
    counter.increment();
    log.error("Returned HTTP Status 500 due to the following exception:", e);
    return INTERNAL_SERVER_ERROR;
  }
}

