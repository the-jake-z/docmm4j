package com.jzarob.docmm4j.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class DuplicateDocumentException extends RuntimeException {
}
