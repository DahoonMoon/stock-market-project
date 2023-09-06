package com.project.stock.common.exception;

import com.project.stock.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class PagingException extends RuntimeException{

	private ErrorCode errorCode;
	private String value;

	public PagingException(String value, String messeage, ErrorCode errorCode) {
		super(messeage);
		this.value = value;
		this.errorCode = errorCode;
	}

	public PagingException(String value, ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.value = value;
		this.errorCode = errorCode;
	}

}
