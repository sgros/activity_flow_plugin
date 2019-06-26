// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import java.io.IOException;
import com.google.android.exoplayer2.util.MediaClock;
import com.google.android.exoplayer2.source.SampleStream;

public interface Renderer extends Target
{
    void disable();
    
    void enable(final RendererConfiguration p0, final Format[] p1, final SampleStream p2, final long p3, final boolean p4, final long p5) throws ExoPlaybackException;
    
    RendererCapabilities getCapabilities();
    
    MediaClock getMediaClock();
    
    int getState();
    
    SampleStream getStream();
    
    int getTrackType();
    
    boolean hasReadStreamToEnd();
    
    boolean isCurrentStreamFinal();
    
    boolean isEnded();
    
    boolean isReady();
    
    void maybeThrowStreamError() throws IOException;
    
    void render(final long p0, final long p1) throws ExoPlaybackException;
    
    void replaceStream(final Format[] p0, final SampleStream p1, final long p2) throws ExoPlaybackException;
    
    void reset();
    
    void resetPosition(final long p0) throws ExoPlaybackException;
    
    void setCurrentStreamFinal();
    
    void setIndex(final int p0);
    
    void setOperatingRate(final float p0) throws ExoPlaybackException;
    
    void start() throws ExoPlaybackException;
    
    void stop() throws ExoPlaybackException;
}
