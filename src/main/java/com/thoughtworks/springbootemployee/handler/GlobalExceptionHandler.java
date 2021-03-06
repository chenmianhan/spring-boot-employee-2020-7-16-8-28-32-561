package com.thoughtworks.springbootemployee.handler;

import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchDataException.class)
    String handleNoSuchDataException(NoSuchDataException noSuchDataException) {
        return noSuchDataException.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalOperationException.class)
    String handleIllegalOperationException(IllegalOperationException illegalOperationException) {
        return illegalOperationException.getMessage();
    }

}
