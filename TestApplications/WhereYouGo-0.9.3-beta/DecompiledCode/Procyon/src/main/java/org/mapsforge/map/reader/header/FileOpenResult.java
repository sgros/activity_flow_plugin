// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader.header;

public class FileOpenResult
{
    public static final FileOpenResult SUCCESS;
    private final String errorMessage;
    private final boolean success;
    
    static {
        SUCCESS = new FileOpenResult();
    }
    
    private FileOpenResult() {
        this.success = true;
        this.errorMessage = null;
    }
    
    public FileOpenResult(final String errorMessage) {
        if (errorMessage == null) {
            throw new IllegalArgumentException("error message must not be null");
        }
        this.success = false;
        this.errorMessage = errorMessage;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public boolean isSuccess() {
        return this.success;
    }
}
