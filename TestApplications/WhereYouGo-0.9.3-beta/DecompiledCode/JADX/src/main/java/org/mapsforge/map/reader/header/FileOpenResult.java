package org.mapsforge.map.reader.header;

public class FileOpenResult {
    public static final FileOpenResult SUCCESS = new FileOpenResult();
    private final String errorMessage;
    private final boolean success;

    public FileOpenResult(String errorMessage) {
        if (errorMessage == null) {
            throw new IllegalArgumentException("error message must not be null");
        }
        this.success = false;
        this.errorMessage = errorMessage;
    }

    private FileOpenResult() {
        this.success = true;
        this.errorMessage = null;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean isSuccess() {
        return this.success;
    }
}
