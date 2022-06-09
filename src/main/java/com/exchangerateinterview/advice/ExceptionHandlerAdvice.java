package com.exchangerateinterview.advice;

import com.exchangerateinterview.util.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity handleException(ResponseStatusException e) {
        new Logger(e.getStackTrace()[0].getClassName()).error(e.getReason());
        return ResponseEntity
                .status(e.getStatus())
                .body(e.getReason());
    }
}

