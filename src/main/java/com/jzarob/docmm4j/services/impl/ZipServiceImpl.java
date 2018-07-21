package com.jzarob.docmm4j.services.impl;

import com.jzarob.docmm4j.services.ZipService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipServiceImpl implements ZipService {


    // Shamelessly stolen from:
    // https://stackoverflow.com/questions/15968883/how-to-zip-a-folder-itself-using-java
    @Override
    public void zipDirectory(Path sourceDirPath, OutputStream outputStream) throws IOException {
        try (ZipOutputStream zs = new ZipOutputStream(outputStream)) {
            Files.walk(sourceDirPath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
        }
    }
}
