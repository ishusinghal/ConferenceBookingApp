package com.mashreq.mashreqconferencebooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({DateTimeException.class})
    public ResponseEntity<Object> handleInvalidDateRangeException(DateTimeException exception) {
        ResponseException responseException = new ResponseException();
        responseException.setStatus("ERROR");
        responseException.setMessage(exception.getMessage());
        responseException.setErrorDetails("Error Date time exception");
        responseException.setErrorCode("ERR_DT_111");

        return new ResponseEntity(responseException, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException exception, ErroCode erroCode) {
        ResponseException responseException = new ResponseException();
        responseException.setStatus("ERROR");
        responseException.setMessage(erroCode.getErrorCode());
        responseException.setErrorDetails("Error Runtime Exception");
        responseException.setErrorCode("ERR_RUNTIME_EX_1101");

        return new ResponseEntity(responseException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception) {

        ResponseException responseException = new ResponseException();
        responseException.setStatus("ERROR");
        responseException.setMessage(exception.getMessage());
        responseException.setErrorDetails("Error Exception");
        responseException.setErrorCode("ERR_EXCEPTION_1102");

        return new ResponseEntity(responseException, HttpStatus.BAD_REQUEST);

    }

}
