// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

public class DecryptionException extends Exception
{
    public final int errorCode;
    
    public DecryptionException(final int errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
