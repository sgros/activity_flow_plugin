// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import java.nio.ByteBuffer;
import com.google.android.exoplayer2.PlaybackParameters;

public interface AudioSink
{
    void configure(final int p0, final int p1, final int p2, final int p3, final int[] p4, final int p5, final int p6) throws ConfigurationException;
    
    void disableTunneling();
    
    void enableTunnelingV21(final int p0);
    
    void flush();
    
    long getCurrentPositionUs(final boolean p0);
    
    PlaybackParameters getPlaybackParameters();
    
    boolean handleBuffer(final ByteBuffer p0, final long p1) throws InitializationException, WriteException;
    
    void handleDiscontinuity();
    
    boolean hasPendingData();
    
    boolean isEnded();
    
    void pause();
    
    void play();
    
    void playToEndOfStream() throws WriteException;
    
    void reset();
    
    void setAudioAttributes(final AudioAttributes p0);
    
    void setAuxEffectInfo(final AuxEffectInfo p0);
    
    void setListener(final Listener p0);
    
    PlaybackParameters setPlaybackParameters(final PlaybackParameters p0);
    
    void setVolume(final float p0);
    
    boolean supportsOutput(final int p0, final int p1);
    
    public static final class WriteException extends Exception
    {
        public final int errorCode;
        
        public WriteException(final int n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("AudioTrack write failed: ");
            sb.append(n);
            super(sb.toString());
            this.errorCode = n;
        }
    }
    
    public static final class InitializationException extends Exception
    {
        public final int audioTrackState;
        
        public InitializationException(final int n, final int i, final int j, final int k) {
            final StringBuilder sb = new StringBuilder();
            sb.append("AudioTrack init failed: ");
            sb.append(n);
            sb.append(", Config(");
            sb.append(i);
            sb.append(", ");
            sb.append(j);
            sb.append(", ");
            sb.append(k);
            sb.append(")");
            super(sb.toString());
            this.audioTrackState = n;
        }
    }
    
    public static final class ConfigurationException extends Exception
    {
        public ConfigurationException(final String message) {
            super(message);
        }
        
        public ConfigurationException(final Throwable cause) {
            super(cause);
        }
    }
    
    public interface Listener
    {
        void onAudioSessionId(final int p0);
        
        void onPositionDiscontinuity();
        
        void onUnderrun(final int p0, final long p1, final long p2);
    }
}
