// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.PlaybackParameters;

public final class StandaloneMediaClock implements MediaClock
{
    private long baseElapsedMs;
    private long baseUs;
    private final Clock clock;
    private PlaybackParameters playbackParameters;
    private boolean started;
    
    public StandaloneMediaClock(final Clock clock) {
        this.clock = clock;
        this.playbackParameters = PlaybackParameters.DEFAULT;
    }
    
    @Override
    public PlaybackParameters getPlaybackParameters() {
        return this.playbackParameters;
    }
    
    @Override
    public long getPositionUs() {
        long baseUs = this.baseUs;
        if (this.started) {
            final long n = this.clock.elapsedRealtime() - this.baseElapsedMs;
            final PlaybackParameters playbackParameters = this.playbackParameters;
            long n2;
            if (playbackParameters.speed == 1.0f) {
                n2 = C.msToUs(n);
            }
            else {
                n2 = playbackParameters.getMediaTimeUsForPlayoutTimeMs(n);
            }
            baseUs += n2;
        }
        return baseUs;
    }
    
    public void resetPosition(final long baseUs) {
        this.baseUs = baseUs;
        if (this.started) {
            this.baseElapsedMs = this.clock.elapsedRealtime();
        }
    }
    
    @Override
    public PlaybackParameters setPlaybackParameters(final PlaybackParameters playbackParameters) {
        if (this.started) {
            this.resetPosition(this.getPositionUs());
        }
        return this.playbackParameters = playbackParameters;
    }
    
    public void start() {
        if (!this.started) {
            this.baseElapsedMs = this.clock.elapsedRealtime();
            this.started = true;
        }
    }
    
    public void stop() {
        if (this.started) {
            this.resetPosition(this.getPositionUs());
            this.started = false;
        }
    }
}
