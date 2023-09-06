package com.project.stock.common.exception;

import com.project.stock.common.code.ErrorCode;
import com.project.stock.model.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> nullPointerException(Exception e){
        log.error("NullPointerException {}", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.UNKNOWN_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PagingException.class)
    protected ResponseEntity<ErrorResponse> pagingException(PagingException e){
        log.error("PagingException {}", e);
        final ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }

}
