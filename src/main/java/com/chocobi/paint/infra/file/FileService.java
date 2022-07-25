package com.chocobi.paint.infra.file;

import java.io.File;

public interface FileService<T> {
    File save(File file);

    T open(File file);
}
