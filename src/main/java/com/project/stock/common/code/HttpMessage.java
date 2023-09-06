package com.project.stock.common.code;

import org.springframework.http.HttpStatus;

public interface HttpMessage extends Message {

    HttpStatus getHttpStatus();

}
