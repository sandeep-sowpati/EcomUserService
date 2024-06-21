package dev.sansow.ecomuserservice.controller;


import dev.sansow.ecomuserservice.exceptions.SessionExpiredException;
import dev.sansow.ecomuserservice.exceptions.SessionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(value = SessionNotFoundException.class)
    public ResponseEntity<String> sessionNotFoundExceptionHandler(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = SessionExpiredException.class)
    public ResponseEntity<String> sessionExpiredExceptionHandler(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> pendingExceptionHandler(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
