package com.top.antibiotic.exceptionhandler;

import com.top.antibiotic.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExHandler {

    @ExceptionHandler(value = javax.validation.ConstraintViolationException.class)
    public ResponseEntity<Object> handleCategoryAlreadyExistsException(javax.validation.ConstraintViolationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<Object> handle(Exception exception) {
//        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
//    }
}