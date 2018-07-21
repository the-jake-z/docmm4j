package com.jzarob.docmm4j.models;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DocumentMergerTest {

    private DocumentMerger documentMerger;
    private File temp;

    @Before
    public void setUp() throws Exception {

        temp = File.createTempFile("merger-tester", ".docx");

        Map<String, String> mergeData = new HashMap<>();

        mergeData.put("SampleName", "Jake Z");

        this.documentMerger = new DocumentMerger(new FileInputStream("samples/sample.docx"), mergeData);
    }

    @Test
    public void documentMerger_shouldPerformMergeSuccessfully() throws Exception {
        FileOutputStream outputStream = new FileOutputStream(temp);
        this.documentMerger.performMerge(outputStream);
        outputStream.flush();
        outputStream.close();

        Assert.assertTrue(temp.length() > 0);
    }

    @After
    public void tearDown() {
        temp.delete();
    }
}
