// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

public final class RendererConfiguration
{
    public static final RendererConfiguration DEFAULT;
    public final int tunnelingAudioSessionId;
    
    static {
        DEFAULT = new RendererConfiguration(0);
    }
    
    public RendererConfiguration(final int tunnelingAudioSessionId) {
        this.tunnelingAudioSessionId = tunnelingAudioSessionId;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && RendererConfiguration.class == o.getClass()) {
            if (this.tunnelingAudioSessionId != ((RendererConfiguration)o).tunnelingAudioSessionId) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.tunnelingAudioSessionId;
    }
}
