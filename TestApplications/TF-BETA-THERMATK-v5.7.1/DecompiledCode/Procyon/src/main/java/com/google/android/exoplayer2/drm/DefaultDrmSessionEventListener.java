// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

public interface DefaultDrmSessionEventListener
{
    void onDrmKeysLoaded();
    
    void onDrmKeysRestored();
    
    void onDrmSessionAcquired();
    
    void onDrmSessionManagerError(final Exception p0);
    
    void onDrmSessionReleased();
}
