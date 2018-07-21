package com.jzarob.docmm4j.models;

import com.jzarob.docmm4j.exceptions.WordDocumentLoadException;
import com.jzarob.docmm4j.exceptions.WordDocumentMergeException;
import com.jzarob.docmm4j.exceptions.WordDocumentSaveException;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.pdf.PdfConversion;
import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class DocumentMerger {

    static {
        MailMerger.setMERGEFIELDInOutput(MailMerger.OutputField.KEEP_MERGEFIELD);
    }

    private final WordprocessingMLPackage wordprocessingMLPackage;
    private final Map<String, String> mergeData;

    private DocumentMerger() {
        this.wordprocessingMLPackage = null;
        this.mergeData = null;
    }

    public DocumentMerger(InputStream inputStream, Map<String, String> mergeData) {

        this.mergeData = mergeData;

        try {
            this.wordprocessingMLPackage = WordprocessingMLPackage.load(inputStream);
        } catch (Docx4JException exception) {
            throw new WordDocumentLoadException(exception);
        }
    }

    public Map<String, String> getMergeData() {
        return mergeData;
    }

    public WordprocessingMLPackage getWordprocessingMLPackage() {
        return wordprocessingMLPackage;
    }

    private static Map<DataFieldName, String> toDataFieldMap(Map<String, String> mergeData) {
        Map<DataFieldName, String> retValue = new HashMap<>(mergeData.size());

        mergeData.forEach((key, value) -> {
            retValue.put(new DataFieldName(key), value);
        });

        return retValue;
    }

    public void performMerge(OutputStream outputStream) {
        try {
            MailMerger.performMerge(wordprocessingMLPackage, toDataFieldMap(mergeData), true);
        } catch (Docx4JException exception) {
            throw new WordDocumentMergeException(exception);
        }

        try {
            Docx4J.toPDF(wordprocessingMLPackage, outputStream);
        } catch (Docx4JException exception) {
            throw new WordDocumentSaveException(exception);
        }
    }
}
