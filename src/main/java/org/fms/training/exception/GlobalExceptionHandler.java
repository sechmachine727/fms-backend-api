package org.fms.training.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleAllExceptions(Exception ex) {
        // Log lỗi ra console hoặc file
        ex.printStackTrace();
        // Ghi log thông qua framework logging
        // LoggerFactory.getLogger(GlobalExceptionHandler.class).error("Exception caught", ex);
    }
}