// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing;

public final class FormatException extends ReaderException
{
    private static final FormatException INSTANCE;
    
    static {
        (INSTANCE = new FormatException()).setStackTrace(FormatException.NO_TRACE);
    }
    
    private FormatException() {
    }
    
    private FormatException(final Throwable t) {
        super(t);
    }
    
    public static FormatException getFormatInstance() {
        FormatException instance;
        if (FormatException.isStackTrace) {
            instance = new FormatException();
        }
        else {
            instance = FormatException.INSTANCE;
        }
        return instance;
    }
    
    public static FormatException getFormatInstance(final Throwable t) {
        FormatException instance;
        if (FormatException.isStackTrace) {
            instance = new FormatException(t);
        }
        else {
            instance = FormatException.INSTANCE;
        }
        return instance;
    }
}
