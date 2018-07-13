package com.jzarob.docmm4j.models;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class WordDocumentTest {

    private WordDocument wordDocument;

    @Before
    public void setUp() throws Exception {
        this.wordDocument = new WordDocument(new FileInputStream("samples/sample.docx"));
    }

    @Test
    public void merge() throws Exception {

        Map<String, String> mergeData = new HashMap<>();

        mergeData.put("SampleName", "Jake Z");

        this.wordDocument.setMergeData(mergeData);

        FileOutputStream outputStream = new FileOutputStream("sample.docx");

        this.wordDocument.performMerge(outputStream, true);
    }
}
