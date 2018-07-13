package com.jzarob.docmm4j.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class WordDocumentSaveException extends Docmm4jBaseException {
    public WordDocumentSaveException(Exception ex) {
        super(ex);
    }
}
