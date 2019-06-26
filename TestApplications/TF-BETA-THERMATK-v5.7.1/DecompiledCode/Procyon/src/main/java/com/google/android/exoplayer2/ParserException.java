// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import java.io.IOException;

public class ParserException extends IOException
{
    public ParserException() {
    }
    
    public ParserException(final String message) {
        super(message);
    }
    
    public ParserException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ParserException(final Throwable cause) {
        super(cause);
    }
}
