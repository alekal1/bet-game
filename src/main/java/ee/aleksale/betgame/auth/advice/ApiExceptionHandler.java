package ee.aleksale.betgame.auth.advice;

import ee.aleksale.betgame.common.exception.contract.GameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(exception = GameException.class)
    public ResponseEntity<?> handleGameException(GameException e) {
      log.error("Got game exception {}", e.getMessage(), e);

      return ResponseEntity.badRequest().body(e.getMessage());
    }
}
