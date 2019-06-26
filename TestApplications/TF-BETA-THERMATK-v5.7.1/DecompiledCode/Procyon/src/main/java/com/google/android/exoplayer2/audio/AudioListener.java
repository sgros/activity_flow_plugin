// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

public interface AudioListener
{
    void onAudioAttributesChanged(final AudioAttributes p0);
    
    void onAudioSessionId(final int p0);
    
    void onVolumeChanged(final float p0);
}
