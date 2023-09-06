package com.project.stock.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements HttpMessage {

	BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "요청값이 존재하지 않습니다."),
	UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다."),
	PAGING_ERROR(HttpStatus.BAD_REQUEST, "페이징 에러입니다.");

	private final HttpStatus httpStatus;
	private final String message;

	public static ErrorCode of(HttpStatus httpStatus) {
		if (httpStatus.is4xxClientError()) {
			return ErrorCode.BAD_REQUEST_ERROR;
		} else {
			return ErrorCode.UNKNOWN_SERVER_ERROR;
		}
	}

}
