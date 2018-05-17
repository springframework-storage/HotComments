package com.naver.hackday.exception;

import com.naver.hackday.model.codingsquid.ErrorModel;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BaseException.class)
    public @ResponseBody
    ErrorModel handleBaseException(BaseException e) {
        return new ErrorModel(e.getStatusCode(), e.getMessage());
    }

}
