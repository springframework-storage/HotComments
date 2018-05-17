package com.naver.hackday.exception;

import com.naver.hackday.model.codingsquid.ErrorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger("log.hackday");

    @ExceptionHandler(value = BaseException.class)
    public @ResponseBody ErrorModel handleBaseException(BaseException e) {
        logger.debug("GlobalException statusCode : " + e.getStatusCode() + " msg : " + e.getMessage());
        return new ErrorModel(e.getStatusCode(), e.getMessage());
    }

}
