package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenericException extends RuntimeException {

    protected ErrorDTO errorDetail;
    protected HttpStatus httpStatus;

    public GenericException(String message) {
        super(message);
    }
}
