package org.example.friend.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.friend.response.Response;

import org.example.friend.response.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public Response handleException(CustomException e) {
        log.error("报错：" + e.getMessage(), e);
        return ResponseEntity.error(e.getCode(), e.getMessage());
    }
}
