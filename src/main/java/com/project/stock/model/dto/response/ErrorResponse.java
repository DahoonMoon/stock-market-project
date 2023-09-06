package com.project.stock.model.dto.response;

import com.project.stock.common.code.ErrorCode;
import com.project.stock.common.exception.PagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private List<FieldError> errors;

    private ErrorResponse(final ErrorCode code, final List<FieldError> errors){
        this.status = code.getHttpStatus().value();
        this.message = code.getMessage();
        this.errors = errors;
    }

    private ErrorResponse(final ErrorCode code){
        this.status = code.getHttpStatus().value();
        this.message = code.getMessage();
        this.errors = new ArrayList<>();
    }

    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(PagingException e) {
        final List<ErrorResponse.FieldError> errors = FieldError.of("page", e.getValue(), e.getMessage());
        return new ErrorResponse(ErrorCode.BAD_REQUEST_ERROR, errors);
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldError {

        private String field;
        private String value;
        private String reason;

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        public static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = (List<org.springframework.validation.FieldError>) bindingResult.getFieldError();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }

    }


}
