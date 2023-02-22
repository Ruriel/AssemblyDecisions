package com.ruriel.assembly.configuration;

import com.ruriel.assembly.api.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlers {
    private static final String ERROR_KEY = "errors";
    @ExceptionHandler
    public ResponseEntity<Map<String, List<String>>> handle(MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        var body = Map.of(ERROR_KEY, errors);
        return ResponseEntity.badRequest()
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(ResourceNotFoundException exception) {
        var body = Map.of(ERROR_KEY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(ConstraintViolationException exception) {
        var message = Arrays.stream(exception.getCause().getMessage().split(":"))
                .findFirst()
                .orElse(exception.getMessage());
        var body = Map.of(ERROR_KEY, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(EntityNotFoundException exception) {
        var body = Map.of(ERROR_KEY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(VotingHasNotStartedException exception){
        var body = Map.of(ERROR_KEY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(VotingIsFinishedException exception){
        var body = Map.of(ERROR_KEY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(VotingHasAlreadyStartedException exception){
        var body = Map.of(ERROR_KEY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(AssociateNotRegisteredInAgendaException exception){
        var body = Map.of(ERROR_KEY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(AgendaAlreadyHasVotingSessionException exception){
        var body = Map.of(ERROR_KEY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handle(AssociateAlreadyVotedException exception){
        var body = Map.of(ERROR_KEY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
