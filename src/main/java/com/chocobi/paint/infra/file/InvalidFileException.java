package com.chocobi.paint.infra.file;

public class InvalidFileException extends IllegalArgumentException {
    private static final String MESSAGE = "올바르지 않은 파일입니다.";

    public InvalidFileException() {
        super(MESSAGE);
    }
}
