package com.example.rekreativ.error;

import com.example.rekreativ.error.exceptions.IllegalParameterException;
import com.example.rekreativ.error.exceptions.ObjectAlreadyExistsException;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandling {

    @ResponseBody
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<HttpResponse> objectNotFoundException(ObjectNotFoundException e, HttpServletRequest request) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        return createHttpResponse(notFound, e.getMessage(), request.getRequestURI());
    }

    @ResponseBody
    @ExceptionHandler(ObjectAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<HttpResponse> objectAlreadyExistsException(ObjectAlreadyExistsException e, HttpServletRequest request) {
        HttpStatus notFound = HttpStatus.BAD_REQUEST;

        return createHttpResponse(notFound, e.getMessage(), request.getRequestURI());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<HttpResponse> validationException(ConstraintViolationException e, HttpServletRequest request) {
        HttpStatus notFound = HttpStatus.BAD_REQUEST;

        return createHttpResponse(notFound, e.getMessage(), request.getRequestURI());
    }

    @ResponseBody
    @ExceptionHandler(IllegalParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<HttpResponse> illegalParameterException(IllegalParameterException e, HttpServletRequest request) {
        HttpStatus notFound = HttpStatus.BAD_REQUEST;

        return createHttpResponse(notFound, e.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message, String path) {

        return new ResponseEntity<>(new HttpResponse(
                path, message, httpStatus, httpStatus.value(), LocalDateTime.now()), httpStatus);
    }
}
