// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import android.media.AudioTimestamp;
import android.annotation.TargetApi;
import com.google.android.exoplayer2.util.Util;
import android.media.AudioTrack;

final class AudioTimestampPoller
{
    private final AudioTimestampV19 audioTimestamp;
    private long initialTimestampPositionFrames;
    private long initializeSystemTimeUs;
    private long lastTimestampSampleTimeUs;
    private long sampleIntervalUs;
    private int state;
    
    public AudioTimestampPoller(final AudioTrack audioTrack) {
        if (Util.SDK_INT >= 19) {
            this.audioTimestamp = new AudioTimestampV19(audioTrack);
            this.reset();
        }
        else {
            this.audioTimestamp = null;
            this.updateState(3);
        }
    }
    
    private void updateState(final int state) {
        this.state = state;
        if (state != 0) {
            if (state != 1) {
                if (state != 2 && state != 3) {
                    if (state != 4) {
                        throw new IllegalStateException();
                    }
                    this.sampleIntervalUs = 500000L;
                }
                else {
                    this.sampleIntervalUs = 10000000L;
                }
            }
            else {
                this.sampleIntervalUs = 5000L;
            }
        }
        else {
            this.lastTimestampSampleTimeUs = 0L;
            this.initialTimestampPositionFrames = -1L;
            this.initializeSystemTimeUs = System.nanoTime() / 1000L;
            this.sampleIntervalUs = 5000L;
        }
    }
    
    public void acceptTimestamp() {
        if (this.state == 4) {
            this.reset();
        }
    }
    
    public long getTimestampPositionFrames() {
        final AudioTimestampV19 audioTimestamp = this.audioTimestamp;
        long timestampPositionFrames;
        if (audioTimestamp != null) {
            timestampPositionFrames = audioTimestamp.getTimestampPositionFrames();
        }
        else {
            timestampPositionFrames = -1L;
        }
        return timestampPositionFrames;
    }
    
    public long getTimestampSystemTimeUs() {
        final AudioTimestampV19 audioTimestamp = this.audioTimestamp;
        long timestampSystemTimeUs;
        if (audioTimestamp != null) {
            timestampSystemTimeUs = audioTimestamp.getTimestampSystemTimeUs();
        }
        else {
            timestampSystemTimeUs = -9223372036854775807L;
        }
        return timestampSystemTimeUs;
    }
    
    public boolean hasTimestamp() {
        final int state = this.state;
        boolean b = true;
        if (state != 1) {
            b = (state == 2 && b);
        }
        return b;
    }
    
    public boolean isTimestampAdvancing() {
        return this.state == 2;
    }
    
    public boolean maybePollTimestamp(final long lastTimestampSampleTimeUs) {
        final AudioTimestampV19 audioTimestamp = this.audioTimestamp;
        if (audioTimestamp != null && lastTimestampSampleTimeUs - this.lastTimestampSampleTimeUs >= this.sampleIntervalUs) {
            this.lastTimestampSampleTimeUs = lastTimestampSampleTimeUs;
            final boolean maybeUpdateTimestamp = audioTimestamp.maybeUpdateTimestamp();
            final int state = this.state;
            boolean b;
            if (state != 0) {
                if (state != 1) {
                    if (state != 2) {
                        if (state != 3) {
                            if (state != 4) {
                                throw new IllegalStateException();
                            }
                            b = maybeUpdateTimestamp;
                        }
                        else {
                            b = maybeUpdateTimestamp;
                            if (maybeUpdateTimestamp) {
                                this.reset();
                                b = maybeUpdateTimestamp;
                            }
                        }
                    }
                    else {
                        b = maybeUpdateTimestamp;
                        if (!maybeUpdateTimestamp) {
                            this.reset();
                            b = maybeUpdateTimestamp;
                        }
                    }
                }
                else if (maybeUpdateTimestamp) {
                    b = maybeUpdateTimestamp;
                    if (this.audioTimestamp.getTimestampPositionFrames() > this.initialTimestampPositionFrames) {
                        this.updateState(2);
                        b = maybeUpdateTimestamp;
                    }
                }
                else {
                    this.reset();
                    b = maybeUpdateTimestamp;
                }
            }
            else if (maybeUpdateTimestamp) {
                if (this.audioTimestamp.getTimestampSystemTimeUs() >= this.initializeSystemTimeUs) {
                    this.initialTimestampPositionFrames = this.audioTimestamp.getTimestampPositionFrames();
                    this.updateState(1);
                    b = maybeUpdateTimestamp;
                }
                else {
                    b = false;
                }
            }
            else {
                b = maybeUpdateTimestamp;
                if (lastTimestampSampleTimeUs - this.initializeSystemTimeUs > 500000L) {
                    this.updateState(3);
                    b = maybeUpdateTimestamp;
                }
            }
            return b;
        }
        return false;
    }
    
    public void rejectTimestamp() {
        this.updateState(4);
    }
    
    public void reset() {
        if (this.audioTimestamp != null) {
            this.updateState(0);
        }
    }
    
    @TargetApi(19)
    private static final class AudioTimestampV19
    {
        private final AudioTimestamp audioTimestamp;
        private final AudioTrack audioTrack;
        private long lastTimestampPositionFrames;
        private long lastTimestampRawPositionFrames;
        private long rawTimestampFramePositionWrapCount;
        
        public AudioTimestampV19(final AudioTrack audioTrack) {
            this.audioTrack = audioTrack;
            this.audioTimestamp = new AudioTimestamp();
        }
        
        public long getTimestampPositionFrames() {
            return this.lastTimestampPositionFrames;
        }
        
        public long getTimestampSystemTimeUs() {
            return this.audioTimestamp.nanoTime / 1000L;
        }
        
        public boolean maybeUpdateTimestamp() {
            final boolean timestamp = this.audioTrack.getTimestamp(this.audioTimestamp);
            if (timestamp) {
                final long framePosition = this.audioTimestamp.framePosition;
                if (this.lastTimestampRawPositionFrames > framePosition) {
                    ++this.rawTimestampFramePositionWrapCount;
                }
                this.lastTimestampRawPositionFrames = framePosition;
                this.lastTimestampPositionFrames = framePosition + (this.rawTimestampFramePositionWrapCount << 32);
            }
            return timestamp;
        }
    }
}
