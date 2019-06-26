// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.StandaloneMediaClock;
import com.google.android.exoplayer2.util.MediaClock;

final class DefaultMediaClock implements MediaClock
{
    private final PlaybackParameterListener listener;
    private MediaClock rendererClock;
    private Renderer rendererClockSource;
    private final StandaloneMediaClock standaloneMediaClock;
    
    public DefaultMediaClock(final PlaybackParameterListener listener, final Clock clock) {
        this.listener = listener;
        this.standaloneMediaClock = new StandaloneMediaClock(clock);
    }
    
    private void ensureSynced() {
        this.standaloneMediaClock.resetPosition(this.rendererClock.getPositionUs());
        final PlaybackParameters playbackParameters = this.rendererClock.getPlaybackParameters();
        if (!playbackParameters.equals(this.standaloneMediaClock.getPlaybackParameters())) {
            this.standaloneMediaClock.setPlaybackParameters(playbackParameters);
            this.listener.onPlaybackParametersChanged(playbackParameters);
        }
    }
    
    private boolean isUsingRendererClock() {
        final Renderer rendererClockSource = this.rendererClockSource;
        return rendererClockSource != null && !rendererClockSource.isEnded() && (this.rendererClockSource.isReady() || !this.rendererClockSource.hasReadStreamToEnd());
    }
    
    @Override
    public PlaybackParameters getPlaybackParameters() {
        final MediaClock rendererClock = this.rendererClock;
        PlaybackParameters playbackParameters;
        if (rendererClock != null) {
            playbackParameters = rendererClock.getPlaybackParameters();
        }
        else {
            playbackParameters = this.standaloneMediaClock.getPlaybackParameters();
        }
        return playbackParameters;
    }
    
    @Override
    public long getPositionUs() {
        if (this.isUsingRendererClock()) {
            return this.rendererClock.getPositionUs();
        }
        return this.standaloneMediaClock.getPositionUs();
    }
    
    public void onRendererDisabled(final Renderer renderer) {
        if (renderer == this.rendererClockSource) {
            this.rendererClock = null;
            this.rendererClockSource = null;
        }
    }
    
    public void onRendererEnabled(final Renderer rendererClockSource) throws ExoPlaybackException {
        final MediaClock mediaClock = rendererClockSource.getMediaClock();
        if (mediaClock != null) {
            final MediaClock rendererClock = this.rendererClock;
            if (mediaClock != rendererClock) {
                if (rendererClock != null) {
                    throw ExoPlaybackException.createForUnexpected(new IllegalStateException("Multiple renderer media clocks enabled."));
                }
                this.rendererClock = mediaClock;
                this.rendererClockSource = rendererClockSource;
                this.rendererClock.setPlaybackParameters(this.standaloneMediaClock.getPlaybackParameters());
                this.ensureSynced();
            }
        }
    }
    
    public void resetPosition(final long n) {
        this.standaloneMediaClock.resetPosition(n);
    }
    
    @Override
    public PlaybackParameters setPlaybackParameters(final PlaybackParameters playbackParameters) {
        final MediaClock rendererClock = this.rendererClock;
        PlaybackParameters setPlaybackParameters = playbackParameters;
        if (rendererClock != null) {
            setPlaybackParameters = rendererClock.setPlaybackParameters(playbackParameters);
        }
        this.standaloneMediaClock.setPlaybackParameters(setPlaybackParameters);
        this.listener.onPlaybackParametersChanged(setPlaybackParameters);
        return setPlaybackParameters;
    }
    
    public void start() {
        this.standaloneMediaClock.start();
    }
    
    public void stop() {
        this.standaloneMediaClock.stop();
    }
    
    public long syncAndGetPositionUs() {
        if (this.isUsingRendererClock()) {
            this.ensureSynced();
            return this.rendererClock.getPositionUs();
        }
        return this.standaloneMediaClock.getPositionUs();
    }
    
    public interface PlaybackParameterListener
    {
        void onPlaybackParametersChanged(final PlaybackParameters p0);
    }
}
