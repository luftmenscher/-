package com.klook.hotel.task.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常拦截类
 */
@RestControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception e){
        return Result.error(e.getMessage());
    }
}
