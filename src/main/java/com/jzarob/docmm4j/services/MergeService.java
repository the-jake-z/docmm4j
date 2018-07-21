package com.jzarob.docmm4j.services;

import com.jzarob.docmm4j.models.MultiMergeRequest;
import org.springframework.core.io.Resource;

public interface MergeService {
    Resource mergeDocument(String documentNumber, String mergeData);
    Resource mergeMultipleDocuments(MultiMergeRequest multiMergeRequest);
}
