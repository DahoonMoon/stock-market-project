package com.project.stock.model.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class CommonResponse<T> {

    private int status;
    private String message;
    private T data;

    public CommonResponse(HttpStatus status, String message, T data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    public CommonResponse(HttpStatus status, T data) {
        this.status = status.value();
        this.message = status.getReasonPhrase();
        this.data = data;
    }

    public CommonResponse(T data) {
        this.status = 200;
        this.message = "Ok";
        this.data = data;
    }

}
