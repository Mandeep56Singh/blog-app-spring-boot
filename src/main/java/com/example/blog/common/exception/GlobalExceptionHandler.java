package com.example.blog.common.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = ApiError.VALIDATION_FAILED;
        ProblemDetail pd = problemDetailProvider(request, apiError);

        log.warn("Validation failed for request: {}", ex.getBindingResult().getObjectName(), ex);
        // Build detailed field errors
        List<Map<String, Object>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    String message = fieldError.getField() + " " + fieldError.getDefaultMessage();

                    Map<String, Object> error = new HashMap<>();
                    error.put("field", fieldError.getField());
                    error.put("rejectedValue", fieldError.getRejectedValue());
                    error.put("message", message);
                    error.put("violation", fieldError.getCode());
                    return Collections.unmodifiableMap(error);

                })
                .toList();


        pd.setProperty("errors", fieldErrors);

        return new ResponseEntity<>(pd, apiError.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request) {

        log.warn("Constraint violation for request", ex);
        ApiError apiError = ApiError.VALIDATION_FAILED;
        ProblemDetail pd = problemDetailProvider(request, apiError);

        List<Map<String, Object>> violations = ex.getConstraintViolations().stream()
                .map(voilation -> {

                    String message = voilation.getPropertyPath().toString() + " " + voilation.getMessage();
                    Map<String, Object> v = new HashMap<>();
                    v.put("field", voilation.getPropertyPath().toString());
                    v.put("invalidValue", voilation.getInvalidValue());
                    v.put("message", message);
                    v.put("constraint", voilation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

                    return Collections.unmodifiableMap(v);
                })
                .toList();
//

        pd.setProperty("errors", violations);

        return new ResponseEntity<>(pd, apiError.getHttpStatus());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex, WebRequest request) {
        log.error("Unhandled exception for request: ", ex);
        ApiError apiError = ApiError.INTERNAL_SERVER_ERROR;
        ProblemDetail pd = problemDetailProvider(request, apiError);
        return new ResponseEntity<>(pd, apiError.getHttpStatus());
    }

    private ProblemDetail problemDetailProvider(WebRequest request, ApiError apiError) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(apiError.getHttpStatus(), apiError.getTitle());

        // Build the base URL of the application (scheme + host + port + context path).
        // Useful for constructing absolute URLs for documentation, error types, or links irrespective of deploying environment.
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        // Build the full URL of the current request (scheme + host + port + context path + servlet path + query params).
        String requestUrl = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();


        problemDetail.setType(URI.create(baseUrl + apiError.getType()));
        problemDetail.setTitle(apiError.getTitle());
        problemDetail.setDetail(apiError.getDetail());
        problemDetail.setInstance(URI.create(requestUrl));
        problemDetail.setProperty("errorCode", apiError.getErrorCode());
        problemDetail.setProperty("timeStamp", Instant.now().toString());
        return problemDetail;
    }


}
