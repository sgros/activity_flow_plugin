// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.C;
import android.os.SystemClock;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;
import java.lang.reflect.Method;
import android.media.AudioTrack;

final class AudioTrackPositionTracker
{
    private AudioTimestampPoller audioTimestampPoller;
    private AudioTrack audioTrack;
    private int bufferSize;
    private long bufferSizeUs;
    private long endPlaybackHeadPosition;
    private long forceResetWorkaroundTimeMs;
    private Method getLatencyMethod;
    private boolean hasData;
    private boolean isOutputPcm;
    private long lastLatencySampleTimeUs;
    private long lastPlayheadSampleTimeUs;
    private long lastRawPlaybackHeadPosition;
    private long latencyUs;
    private final Listener listener;
    private boolean needsPassthroughWorkarounds;
    private int nextPlayheadOffsetIndex;
    private int outputPcmFrameSize;
    private int outputSampleRate;
    private long passthroughWorkaroundPauseOffset;
    private int playheadOffsetCount;
    private final long[] playheadOffsets;
    private long rawPlaybackHeadWrapCount;
    private long smoothedPlayheadOffsetUs;
    private long stopPlaybackHeadPosition;
    private long stopTimestampUs;
    
    public AudioTrackPositionTracker(final Listener listener) {
        Assertions.checkNotNull(listener);
        this.listener = listener;
        while (true) {
            if (Util.SDK_INT < 18) {
                break Label_0037;
            }
            try {
                this.getLatencyMethod = AudioTrack.class.getMethod("getLatency", (Class<?>[])null);
                this.playheadOffsets = new long[10];
            }
            catch (NoSuchMethodException ex) {
                continue;
            }
            break;
        }
    }
    
    private boolean forceHasPendingData() {
        if (this.needsPassthroughWorkarounds) {
            final AudioTrack audioTrack = this.audioTrack;
            Assertions.checkNotNull(audioTrack);
            if (audioTrack.getPlayState() == 2 && this.getPlaybackHeadPosition() == 0L) {
                return true;
            }
        }
        return false;
    }
    
    private long framesToDurationUs(final long n) {
        return n * 1000000L / this.outputSampleRate;
    }
    
    private long getPlaybackHeadPosition() {
        final AudioTrack audioTrack = this.audioTrack;
        Assertions.checkNotNull(audioTrack);
        final AudioTrack audioTrack2 = audioTrack;
        if (this.stopTimestampUs != -9223372036854775807L) {
            return Math.min(this.endPlaybackHeadPosition, this.stopPlaybackHeadPosition + (SystemClock.elapsedRealtime() * 1000L - this.stopTimestampUs) * this.outputSampleRate / 1000000L);
        }
        final int playState = audioTrack2.getPlayState();
        if (playState == 1) {
            return 0L;
        }
        long lastRawPlaybackHeadPosition;
        final long n = lastRawPlaybackHeadPosition = (0xFFFFFFFFL & (long)audioTrack2.getPlaybackHeadPosition());
        if (this.needsPassthroughWorkarounds) {
            if (playState == 2 && n == 0L) {
                this.passthroughWorkaroundPauseOffset = this.lastRawPlaybackHeadPosition;
            }
            lastRawPlaybackHeadPosition = n + this.passthroughWorkaroundPauseOffset;
        }
        if (Util.SDK_INT <= 28) {
            if (lastRawPlaybackHeadPosition == 0L && this.lastRawPlaybackHeadPosition > 0L && playState == 3) {
                if (this.forceResetWorkaroundTimeMs == -9223372036854775807L) {
                    this.forceResetWorkaroundTimeMs = SystemClock.elapsedRealtime();
                }
                return this.lastRawPlaybackHeadPosition;
            }
            this.forceResetWorkaroundTimeMs = -9223372036854775807L;
        }
        if (this.lastRawPlaybackHeadPosition > lastRawPlaybackHeadPosition) {
            ++this.rawPlaybackHeadWrapCount;
        }
        this.lastRawPlaybackHeadPosition = lastRawPlaybackHeadPosition;
        return lastRawPlaybackHeadPosition + (this.rawPlaybackHeadWrapCount << 32);
    }
    
    private long getPlaybackHeadPositionUs() {
        return this.framesToDurationUs(this.getPlaybackHeadPosition());
    }
    
    private void maybePollAndCheckTimestamp(final long n, final long n2) {
        final AudioTimestampPoller audioTimestampPoller = this.audioTimestampPoller;
        Assertions.checkNotNull(audioTimestampPoller);
        final AudioTimestampPoller audioTimestampPoller2 = audioTimestampPoller;
        if (!audioTimestampPoller2.maybePollTimestamp(n)) {
            return;
        }
        final long timestampSystemTimeUs = audioTimestampPoller2.getTimestampSystemTimeUs();
        final long timestampPositionFrames = audioTimestampPoller2.getTimestampPositionFrames();
        if (Math.abs(timestampSystemTimeUs - n) > 5000000L) {
            this.listener.onSystemTimeUsMismatch(timestampPositionFrames, timestampSystemTimeUs, n, n2);
            audioTimestampPoller2.rejectTimestamp();
        }
        else if (Math.abs(this.framesToDurationUs(timestampPositionFrames) - n2) > 5000000L) {
            this.listener.onPositionFramesMismatch(timestampPositionFrames, timestampSystemTimeUs, n, n2);
            audioTimestampPoller2.rejectTimestamp();
        }
        else {
            audioTimestampPoller2.acceptTimestamp();
        }
    }
    
    private void maybeSampleSyncParams() {
        final long playbackHeadPositionUs = this.getPlaybackHeadPositionUs();
        if (playbackHeadPositionUs == 0L) {
            return;
        }
        final long lastPlayheadSampleTimeUs = System.nanoTime() / 1000L;
        if (lastPlayheadSampleTimeUs - this.lastPlayheadSampleTimeUs >= 30000L) {
            final long[] playheadOffsets = this.playheadOffsets;
            final int nextPlayheadOffsetIndex = this.nextPlayheadOffsetIndex;
            playheadOffsets[nextPlayheadOffsetIndex] = playbackHeadPositionUs - lastPlayheadSampleTimeUs;
            this.nextPlayheadOffsetIndex = (nextPlayheadOffsetIndex + 1) % 10;
            final int playheadOffsetCount = this.playheadOffsetCount;
            if (playheadOffsetCount < 10) {
                this.playheadOffsetCount = playheadOffsetCount + 1;
            }
            this.lastPlayheadSampleTimeUs = lastPlayheadSampleTimeUs;
            this.smoothedPlayheadOffsetUs = 0L;
            int n = 0;
            while (true) {
                final int playheadOffsetCount2 = this.playheadOffsetCount;
                if (n >= playheadOffsetCount2) {
                    break;
                }
                this.smoothedPlayheadOffsetUs += this.playheadOffsets[n] / playheadOffsetCount2;
                ++n;
            }
        }
        if (this.needsPassthroughWorkarounds) {
            return;
        }
        this.maybePollAndCheckTimestamp(lastPlayheadSampleTimeUs, playbackHeadPositionUs);
        this.maybeUpdateLatency(lastPlayheadSampleTimeUs);
    }
    
    private void maybeUpdateLatency(final long lastLatencySampleTimeUs) {
        if (this.isOutputPcm) {
            final Method getLatencyMethod = this.getLatencyMethod;
            if (getLatencyMethod != null && lastLatencySampleTimeUs - this.lastLatencySampleTimeUs >= 500000L) {
                try {
                    final AudioTrack audioTrack = this.audioTrack;
                    Assertions.checkNotNull(audioTrack);
                    final Integer n = (Integer)getLatencyMethod.invoke(audioTrack, new Object[0]);
                    Util.castNonNull(n);
                    this.latencyUs = n * 1000L - this.bufferSizeUs;
                    this.latencyUs = Math.max(this.latencyUs, 0L);
                    if (this.latencyUs > 5000000L) {
                        this.listener.onInvalidLatency(this.latencyUs);
                        this.latencyUs = 0L;
                    }
                }
                catch (Exception ex) {
                    this.getLatencyMethod = null;
                }
                this.lastLatencySampleTimeUs = lastLatencySampleTimeUs;
            }
        }
    }
    
    private static boolean needsPassthroughWorkarounds(final int n) {
        return Util.SDK_INT < 23 && (n == 5 || n == 6);
    }
    
    private void resetSyncParams() {
        this.smoothedPlayheadOffsetUs = 0L;
        this.playheadOffsetCount = 0;
        this.nextPlayheadOffsetIndex = 0;
        this.lastPlayheadSampleTimeUs = 0L;
    }
    
    public int getAvailableBufferSize(final long n) {
        return this.bufferSize - (int)(n - this.getPlaybackHeadPosition() * this.outputPcmFrameSize);
    }
    
    public long getCurrentPositionUs(final boolean b) {
        final AudioTrack audioTrack = this.audioTrack;
        Assertions.checkNotNull(audioTrack);
        if (audioTrack.getPlayState() == 3) {
            this.maybeSampleSyncParams();
        }
        final long n = System.nanoTime() / 1000L;
        final AudioTimestampPoller audioTimestampPoller = this.audioTimestampPoller;
        Assertions.checkNotNull(audioTimestampPoller);
        final AudioTimestampPoller audioTimestampPoller2 = audioTimestampPoller;
        if (!audioTimestampPoller2.hasTimestamp()) {
            long playbackHeadPositionUs;
            if (this.playheadOffsetCount == 0) {
                playbackHeadPositionUs = this.getPlaybackHeadPositionUs();
            }
            else {
                playbackHeadPositionUs = n + this.smoothedPlayheadOffsetUs;
            }
            long n2 = playbackHeadPositionUs;
            if (!b) {
                n2 = playbackHeadPositionUs - this.latencyUs;
            }
            return n2;
        }
        final long framesToDurationUs = this.framesToDurationUs(audioTimestampPoller2.getTimestampPositionFrames());
        if (!audioTimestampPoller2.isTimestampAdvancing()) {
            return framesToDurationUs;
        }
        return framesToDurationUs + (n - audioTimestampPoller2.getTimestampSystemTimeUs());
    }
    
    public void handleEndOfStream(final long endPlaybackHeadPosition) {
        this.stopPlaybackHeadPosition = this.getPlaybackHeadPosition();
        this.stopTimestampUs = SystemClock.elapsedRealtime() * 1000L;
        this.endPlaybackHeadPosition = endPlaybackHeadPosition;
    }
    
    public boolean hasPendingData(final long n) {
        return n > this.getPlaybackHeadPosition() || this.forceHasPendingData();
    }
    
    public boolean isPlaying() {
        final AudioTrack audioTrack = this.audioTrack;
        Assertions.checkNotNull(audioTrack);
        return audioTrack.getPlayState() == 3;
    }
    
    public boolean isStalled(final long n) {
        return this.forceResetWorkaroundTimeMs != -9223372036854775807L && n > 0L && SystemClock.elapsedRealtime() - this.forceResetWorkaroundTimeMs >= 200L;
    }
    
    public boolean mayHandleBuffer(final long n) {
        final AudioTrack audioTrack = this.audioTrack;
        Assertions.checkNotNull(audioTrack);
        final int playState = audioTrack.getPlayState();
        if (this.needsPassthroughWorkarounds) {
            if (playState == 2) {
                return this.hasData = false;
            }
            if (playState == 1 && this.getPlaybackHeadPosition() == 0L) {
                return false;
            }
        }
        final boolean hasData = this.hasData;
        this.hasData = this.hasPendingData(n);
        if (hasData && !this.hasData && playState != 1) {
            final Listener listener = this.listener;
            if (listener != null) {
                listener.onUnderrun(this.bufferSize, C.usToMs(this.bufferSizeUs));
            }
        }
        return true;
    }
    
    public boolean pause() {
        this.resetSyncParams();
        if (this.stopTimestampUs == -9223372036854775807L) {
            final AudioTimestampPoller audioTimestampPoller = this.audioTimestampPoller;
            Assertions.checkNotNull(audioTimestampPoller);
            audioTimestampPoller.reset();
            return true;
        }
        return false;
    }
    
    public void reset() {
        this.resetSyncParams();
        this.audioTrack = null;
        this.audioTimestampPoller = null;
    }
    
    public void setAudioTrack(final AudioTrack audioTrack, final int n, final int outputPcmFrameSize, final int bufferSize) {
        this.audioTrack = audioTrack;
        this.outputPcmFrameSize = outputPcmFrameSize;
        this.bufferSize = bufferSize;
        this.audioTimestampPoller = new AudioTimestampPoller(audioTrack);
        this.outputSampleRate = audioTrack.getSampleRate();
        this.needsPassthroughWorkarounds = needsPassthroughWorkarounds(n);
        this.isOutputPcm = Util.isEncodingLinearPcm(n);
        long framesToDurationUs;
        if (this.isOutputPcm) {
            framesToDurationUs = this.framesToDurationUs(bufferSize / outputPcmFrameSize);
        }
        else {
            framesToDurationUs = -9223372036854775807L;
        }
        this.bufferSizeUs = framesToDurationUs;
        this.lastRawPlaybackHeadPosition = 0L;
        this.rawPlaybackHeadWrapCount = 0L;
        this.passthroughWorkaroundPauseOffset = 0L;
        this.hasData = false;
        this.stopTimestampUs = -9223372036854775807L;
        this.forceResetWorkaroundTimeMs = -9223372036854775807L;
        this.latencyUs = 0L;
    }
    
    public void start() {
        final AudioTimestampPoller audioTimestampPoller = this.audioTimestampPoller;
        Assertions.checkNotNull(audioTimestampPoller);
        audioTimestampPoller.reset();
    }
    
    public interface Listener
    {
        void onInvalidLatency(final long p0);
        
        void onPositionFramesMismatch(final long p0, final long p1, final long p2, final long p3);
        
        void onSystemTimeUsMismatch(final long p0, final long p1, final long p2, final long p3);
        
        void onUnderrun(final int p0, final long p1);
    }
}
