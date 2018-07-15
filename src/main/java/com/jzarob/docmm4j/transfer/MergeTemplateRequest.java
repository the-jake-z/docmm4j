package com.jzarob.docmm4j.transfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

public final class MergeTemplateRequest {
    private String documentNumber;
    private String mergeData;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getMergeData() {
        return mergeData;
    }

    public void setMergeData(JsonNode jsonNode) {
        this.mergeData = jsonNode.toString();
    }

    @JsonIgnore
    public void setMergeData(String mergeData) {
        this.mergeData = mergeData;
    }
}
