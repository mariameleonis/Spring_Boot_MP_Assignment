package com.example.booking.exception.handler;

import static com.example.booking.exception.handler.ControllerExceptionHandler.INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.example.booking.exception.BusinessException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ControllerExceptionHandlerTest {

  @Test
  void handleInternalError() throws Exception {
    val meterRegistry = mock(MeterRegistry.class);
    val counter = mock(Counter.class);
    val controllerExceptionHandler = new ControllerExceptionHandler(meterRegistry);
    val exception = mock(Exception.class);

    ReflectionTestUtils.setField(controllerExceptionHandler, "counter", counter);

    val result = controllerExceptionHandler.handleInternalError(exception);

    assertEquals(INTERNAL_SERVER_ERROR, result);

    verify(counter).increment();
  }

  @Test
  void handleInternalError_whenExceptionThrown() throws Exception {
    val meterRegistry = mock(MeterRegistry.class);
    val counter = mock(Counter.class);
    val controllerExceptionHandler = new ControllerExceptionHandler(meterRegistry);
    val exception = mock(BusinessException.class);

    ReflectionTestUtils.setField(controllerExceptionHandler, "counter", counter);

    assertThrows(BusinessException.class, () -> controllerExceptionHandler.handleInternalError(exception));

    verifyNoInteractions(counter);
  }
}