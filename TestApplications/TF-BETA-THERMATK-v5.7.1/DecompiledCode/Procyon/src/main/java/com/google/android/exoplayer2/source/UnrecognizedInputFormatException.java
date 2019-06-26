// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import android.net.Uri;
import com.google.android.exoplayer2.ParserException;

public class UnrecognizedInputFormatException extends ParserException
{
    public final Uri uri;
    
    public UnrecognizedInputFormatException(final String s, final Uri uri) {
        super(s);
        this.uri = uri;
    }
}
