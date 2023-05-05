package com.mentorship.vineservice.handler;

import com.mentorship.vineservice.dto.response.ErrorResponse;
import com.mentorship.vineservice.controller.exeption.PermissionException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()));
    }

    @ExceptionHandler(value = { IllegalArgumentException.class, NoSuchElementException.class })
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()));
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<Object> handleVinePermissionException(PermissionException exception) {
        return buildResponseEntity(exception.getStatus(), List.of(exception.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> customErrors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String defaultMessage = fieldError.getDefaultMessage();
            customErrors.add(defaultMessage);
        }

        return buildResponseEntity(HttpStatus.BAD_REQUEST, customErrors);

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMessage = ex.getMessage();
        if (ex.getRootCause() != null) {
            Throwable rootCause = ex.getRootCause();
            if (rootCause.getLocalizedMessage() != null) {
                errorMessage = ex.getRootCause().getLocalizedMessage();
            }
        }
        return buildResponseEntity(HttpStatus.BAD_REQUEST, List.of(errorMessage));
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, List<String> errors) {
        return new ResponseEntity<>(new ErrorResponse(status, LocalDateTime.now(), errors), status);
    }

}
