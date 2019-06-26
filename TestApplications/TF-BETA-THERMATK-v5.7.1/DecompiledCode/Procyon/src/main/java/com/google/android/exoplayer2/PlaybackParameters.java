// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Assertions;

public final class PlaybackParameters
{
    public static final PlaybackParameters DEFAULT;
    public final float pitch;
    private final int scaledUsPerMs;
    public final boolean skipSilence;
    public final float speed;
    
    static {
        DEFAULT = new PlaybackParameters(1.0f);
    }
    
    public PlaybackParameters(final float n) {
        this(n, 1.0f, false);
    }
    
    public PlaybackParameters(final float n, final float n2) {
        this(n, n2, false);
    }
    
    public PlaybackParameters(final float speed, final float pitch, final boolean skipSilence) {
        final boolean b = true;
        Assertions.checkArgument(speed > 0.0f);
        Assertions.checkArgument(pitch > 0.0f && b);
        this.speed = speed;
        this.pitch = pitch;
        this.skipSilence = skipSilence;
        this.scaledUsPerMs = Math.round(speed * 1000.0f);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && PlaybackParameters.class == o.getClass()) {
            final PlaybackParameters playbackParameters = (PlaybackParameters)o;
            if (this.speed != playbackParameters.speed || this.pitch != playbackParameters.pitch || this.skipSilence != playbackParameters.skipSilence) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    public long getMediaTimeUsForPlayoutTimeMs(final long n) {
        return n * this.scaledUsPerMs;
    }
    
    @Override
    public int hashCode() {
        return ((527 + Float.floatToRawIntBits(this.speed)) * 31 + Float.floatToRawIntBits(this.pitch)) * 31 + (this.skipSilence ? 1 : 0);
    }
}
