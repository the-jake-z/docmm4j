package com.jzarob.docmm4j.services;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

public interface ZipService {
    void zipDirectory(Path sourceDirPath, OutputStream outputStream) throws IOException;
}
