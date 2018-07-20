package com.jzarob.docmm4j.models;

import java.util.List;

public class MultiMergeRequest {
    public List<Groupings> getGroupings() {
        return groupings;
    }

    public void setGroupings(List<Groupings> groupings) {
        this.groupings = groupings;
    }

    private List<Groupings> groupings;
}
