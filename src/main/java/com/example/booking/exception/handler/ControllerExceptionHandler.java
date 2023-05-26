package com.example.booking.exception.handler;

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

  private final Counter counter;

  public ControllerExceptionHandler(MeterRegistry meterRegistry) {
    counter = meterRegistry.counter("http.status.500.counter");
  }

  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  @ResponseBody
  public String handleInternalError(Exception e) {
    counter.increment();
    log.error("Returned HTTP Status 500 due to the following exception:", e);
    return "Internal Server Error";
  }
}
