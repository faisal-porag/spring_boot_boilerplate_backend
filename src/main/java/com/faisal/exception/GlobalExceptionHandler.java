package com.faisal.exception;

import com.faisal.dto.v1.CustomResponse;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<Object>> handleValidationError(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.badRequest().body(
                new CustomResponse<>(
                        "REQUEST_VALIDATION_FAILED",
                        false,
                        errorMessage,
                        null
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomResponse<Object>> handleBadRequest(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new CustomResponse<>(
                        "BAD_REQUEST",
                        false,
                        "Your request is failed: " + ex.getMessage(),
                        null
                )
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CustomResponse<Object>> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new CustomResponse<>(
                        "REQUEST_NOT_FOUND",
                        false,
                        "Resource not found.",
                        null
                )
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomResponse<Object>> handleInvalidJson(HttpMessageNotReadableException ex) {
        String technicalErrMessage = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "Malformed JSON request";
        String errorMessage = "The provided information is not valid";
        return ResponseEntity.badRequest().body(
            new CustomResponse<>(
                    "REQUEST_DATA_INVALID",
                    false,
                    errorMessage,
                    null
            )
        );
    }

    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<CustomResponse<Object>> handleMismatchedInput(MismatchedInputException ex) {
        return ResponseEntity.badRequest().body(
                new CustomResponse<>(
                        "REQUEST_DATA_INVALID",
                        false,
                        "Invalid input type: " + ex.getOriginalMessage(),
                        null
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Object>> handleInternalServerError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new CustomResponse<>(
                        "INTERNAL_SERVICE_ERROR",
                        false,
                        "Your request is failed.",
                        null
                )
        );
    }
}
