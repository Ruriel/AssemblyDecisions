package com.ruriel.assembly.configuration;

import com.ruriel.assembly.api.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionHandlers {
    private static final String ERROR_KEY = "errors";

    @Value("${spring.jackson.date-format}")
    private String dateFormat;

    @ExceptionHandler
    public ResponseEntity<Map<String, List<String>>> handle(MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        log.error(exception.getMessage());
        var body = Map.of(ERROR_KEY, errors);
        return ResponseEntity.badRequest()
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(ResourceNotFoundException exception) {
        var body = Map.of(ERROR_KEY, exception.getMessage());
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(ConstraintViolationException exception) {
        var message = Arrays.stream(exception.getCause().getMessage().split(":"))
                .findFirst()
                .orElse(exception.getMessage());
        var body = Map.of(ERROR_KEY, message);
        log.error(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(EntityNotFoundException exception) {
        var body = Map.of(ERROR_KEY, exception.getMessage());
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(BadRequestException exception) {
        var body = Map.of(ERROR_KEY, exception.getMessage());
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(HttpMessageNotReadableException exception) {
        var specificCause = exception.getMostSpecificCause();
        String message;
        if (specificCause instanceof DateTimeParseException) {
            message = "Dates must match the following pattern: " + dateFormat;
            log.error(message);
        }
        else {
            message = exception.getMessage();
            log.error(message, exception);
        }
        var body = Map.of(ERROR_KEY, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(Exception exception) {
        var message = "Unknown error occurred.";
        var body = Map.of(ERROR_KEY, message);
        log.error(message, exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
