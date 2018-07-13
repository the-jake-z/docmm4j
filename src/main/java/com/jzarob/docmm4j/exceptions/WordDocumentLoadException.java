package com.jzarob.docmm4j.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class WordDocumentLoadException extends Docmm4jBaseException {
    public WordDocumentLoadException(Exception ex) {
        super(ex);
    }
}
