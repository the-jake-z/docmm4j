package com.jzarob.docmm4j.models;

import org.springframework.http.MediaType;


public final class MediaTypes {

    private static final String WORD_STRING = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final MediaType WORD_MEDIA_TYPE = MediaType.parseMediaType(WORD_STRING);

    private MediaTypes() { }
}
