// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing;

public final class ChecksumException extends ReaderException
{
    private static final ChecksumException INSTANCE;
    
    static {
        (INSTANCE = new ChecksumException()).setStackTrace(ChecksumException.NO_TRACE);
    }
    
    private ChecksumException() {
    }
    
    private ChecksumException(final Throwable t) {
        super(t);
    }
    
    public static ChecksumException getChecksumInstance() {
        ChecksumException instance;
        if (ChecksumException.isStackTrace) {
            instance = new ChecksumException();
        }
        else {
            instance = ChecksumException.INSTANCE;
        }
        return instance;
    }
    
    public static ChecksumException getChecksumInstance(final Throwable t) {
        ChecksumException instance;
        if (ChecksumException.isStackTrace) {
            instance = new ChecksumException(t);
        }
        else {
            instance = ChecksumException.INSTANCE;
        }
        return instance;
    }
}
