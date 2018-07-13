package com.jzarob.docmm4j.transfer;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

public final class MergeTemplateRequest {
    private String formNumber;
    private String mergeData;

    public String getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    public String getMergeData() {
        return mergeData;
    }

    public void setMergeData(JsonNode jsonNode) {
        this.mergeData = jsonNode.toString();
    }
}
