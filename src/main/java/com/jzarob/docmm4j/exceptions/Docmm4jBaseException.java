package com.jzarob.docmm4j.exceptions;

public class Docmm4jBaseException extends RuntimeException {
    private final Exception innerException;

    public Docmm4jBaseException(Exception innerException) {
        this.innerException = innerException;
    }

    public Exception getInnerException() {
        return innerException;
    }
}
