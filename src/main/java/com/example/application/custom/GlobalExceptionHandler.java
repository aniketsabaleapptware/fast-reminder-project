package com.example.application.custom;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<CustomResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    CustomResponse customResponse = CustomResponse.builder()
        .message(ex.getMessage())
        .data(null)
        .build();
    return ResponseEntity.badRequest().body(customResponse);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<CustomResponse> handleRuntimeException(RuntimeException ex){
    CustomResponse customResponse = CustomResponse.builder()
        .message(ex.getMessage())
        .data(null)
        .build();
    return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomResponse> handleGlobalException(Exception ex) {
    CustomResponse customResponse = CustomResponse.builder()
        .message("An unexpected error occurred: " + ex.getMessage())
        .data(null)
        .build();
    return new ResponseEntity<>(customResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
