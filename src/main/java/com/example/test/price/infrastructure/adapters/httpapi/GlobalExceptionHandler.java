package com.example.test.price.infrastructure.adapters.httpapi;

import com.example.test.price.domain.exceptions.PriceNotFoundException;
import com.example.test.price.infrastructure.adapters.httpapi.dtos.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePriceNotFoundException(
            PriceNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(
            MissingServletRequestParameterException ex, WebRequest request) {

        String paramName = ex.getParameterName();
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Required parameter '" + paramName + "' is missing",
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        String paramName = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Parameter '" + paramName + "' must be of type " + requiredType,
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(
            DateTimeParseException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Invalid date format: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}