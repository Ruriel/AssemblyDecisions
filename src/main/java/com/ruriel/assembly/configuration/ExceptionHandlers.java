package com.ruriel.assembly.configuration;

import com.ruriel.assembly.api.exceptions.AgendaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler
    public ResponseEntity<?> handle(MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        var body = Map.of("errors", errors);
        return ResponseEntity.badRequest()
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<?> handle(AgendaNotFoundException exception) {
        var body = Map.of("error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }
}
