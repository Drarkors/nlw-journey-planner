package com.rockeseat.nlw.journey.planner.exception.handler;

import com.rockeseat.nlw.journey.planner.activity.exception.InvalidActivityDateException;
import com.rockeseat.nlw.journey.planner.exception.dto.ErrorMessage;
import com.rockeseat.nlw.journey.planner.participant.exception.ParticipantNotFoundException;
import com.rockeseat.nlw.journey.planner.trip.exceptions.TripInvalidStartDateException;
import com.rockeseat.nlw.journey.planner.trip.exceptions.TripNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

  private final MessageSource messageSource;

  @Autowired
  public ControllerExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ErrorMessage>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    var errors = new ArrayList<ErrorMessage>();

    exception.getBindingResult().getFieldErrors().forEach(err -> {
      String message = messageSource.getMessage(err, LocaleContextHolder.getLocale());
      var error = new ErrorMessage(message, err.getField());
      errors.add(error);
    });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(TripInvalidStartDateException.class)
  public ResponseEntity<ErrorMessage> handleTripInvalidStartDateException(TripInvalidStartDateException exception) {
    var errors = new ErrorMessage(exception.getMessage(), "start_at");

    return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(InvalidActivityDateException.class)
  public ResponseEntity<ErrorMessage> handleInvalidActivityDateException(InvalidActivityDateException exception) {
    var errors = new ErrorMessage(exception.getMessage(), "occurs_at");

    return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
  }


  @ExceptionHandler({TripNotFoundException.class, ParticipantNotFoundException.class})
  public ResponseEntity<?> handleNotFoundException(RuntimeException notFoundException) {
    log.error(notFoundException.getMessage());
    return ResponseEntity.notFound().build();
  }

}
