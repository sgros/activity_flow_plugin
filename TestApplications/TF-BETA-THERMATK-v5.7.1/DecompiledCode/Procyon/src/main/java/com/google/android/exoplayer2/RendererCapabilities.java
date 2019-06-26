// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

public interface RendererCapabilities
{
    int getTrackType();
    
    int supportsFormat(final Format p0) throws ExoPlaybackException;
    
    int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException;
}
