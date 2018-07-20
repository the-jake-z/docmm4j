package com.jzarob.docmm4j.models;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DocumentMergerTest {

    private DocumentMerger documentMerger;

    @Before
    public void setUp() throws Exception {

        Map<String, String> mergeData = new HashMap<>();

        mergeData.put("SampleName", "Jake Z");

        this.documentMerger = new DocumentMerger(new FileInputStream("samples/sample.docx"), mergeData);
    }

    @Test
    public void documentMerger_shouldPerformMergeSuccessfully() throws Exception {
        FileOutputStream outputStream = new FileOutputStream("sample.docx");
        this.documentMerger.performMerge(outputStream);
    }
}
