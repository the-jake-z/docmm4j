package com.jzarob.docmm4j.services;

import com.jzarob.docmm4j.models.Document;
import org.springframework.core.io.Resource;

public interface MergeService {
    Resource mergeDocument(Document document, String mergeData);
}
