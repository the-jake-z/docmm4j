package com.jzarob.docmm4j.models;

import java.util.List;

public class Groupings {
    private String name;
    private List<MergeTemplateRequest> mergeRequests;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MergeTemplateRequest> getMergeRequests() {
        return mergeRequests;
    }

    public void setMergeRequests(List<MergeTemplateRequest> mergeRequests) {
        this.mergeRequests = mergeRequests;
    }
}
