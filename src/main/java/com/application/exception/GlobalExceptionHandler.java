package com.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessGlobalException.class)
    protected ResponseEntity<ApiError> handleGlobalException(BusinessGlobalException bgException,
                                                             Locale locale) {
        return new ResponseEntity<>(new ApiError(new Date(), Arrays.toString(bgException.getStackTrace()),
                bgException.getErrorCode(), bgException.getMessage(), locale.getCountry(),
                "", bgException.getServiceName(), bgException.getRequestedUrl()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServerException.class)
    protected ResponseEntity<ApiError> handleServerException(ServerException serverException,
                                                             Locale locale) {
        ApiError apiError = new ApiError(new Date(), serverException.getErrorCode(), serverException.getMessage(),
                serverException.getStatusCode(), locale.getCountry(), serverException.getServiceName(),
                serverException.getRequestedUrl(), "Hidden for Security Reasons");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ApiError> handleException(Exception e, Locale locale) {
        return new ResponseEntity<>(new ApiError(new Date(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), locale.getCountry(),
                Arrays.stream(e.getStackTrace()).findFirst().get().getClassName(),
                null, "Hidden for Security Reasons"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
