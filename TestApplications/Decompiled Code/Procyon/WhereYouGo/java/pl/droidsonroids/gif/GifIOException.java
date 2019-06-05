// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.support.annotation.NonNull;
import java.io.IOException;

public class GifIOException extends IOException
{
    private static final long serialVersionUID = 13038402904505L;
    private final String mErrnoMessage;
    @NonNull
    public final GifError reason;
    
    private GifIOException(final int n, final String mErrnoMessage) {
        this.reason = GifError.fromCode(n);
        this.mErrnoMessage = mErrnoMessage;
    }
    
    static GifIOException fromCode(final int n) {
        GifIOException ex = null;
        if (n != GifError.NO_ERROR.errorCode) {
            ex = new GifIOException(n, null);
        }
        return ex;
    }
    
    @Override
    public String getMessage() {
        String s;
        if (this.mErrnoMessage == null) {
            s = this.reason.getFormattedDescription();
        }
        else {
            s = this.reason.getFormattedDescription() + ": " + this.mErrnoMessage;
        }
        return s;
    }
}
