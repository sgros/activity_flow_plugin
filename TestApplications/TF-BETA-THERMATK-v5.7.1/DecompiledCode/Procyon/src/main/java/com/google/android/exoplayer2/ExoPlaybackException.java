// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import java.io.IOException;

public final class ExoPlaybackException extends Exception
{
    private final Throwable cause;
    public final int rendererIndex;
    public final int type;
    
    private ExoPlaybackException(final int type, final Throwable t, final int rendererIndex) {
        super(t);
        this.type = type;
        this.cause = t;
        this.rendererIndex = rendererIndex;
    }
    
    public static ExoPlaybackException createForRenderer(final Exception ex, final int n) {
        return new ExoPlaybackException(1, ex, n);
    }
    
    public static ExoPlaybackException createForSource(final IOException ex) {
        return new ExoPlaybackException(0, ex, -1);
    }
    
    static ExoPlaybackException createForUnexpected(final RuntimeException ex) {
        return new ExoPlaybackException(2, ex, -1);
    }
}
