package com.jzarob.docmm4j.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MultiMergeException extends Docmm4jBaseException {
    public MultiMergeException(Exception innerException) {
        super(innerException);
    }
}
