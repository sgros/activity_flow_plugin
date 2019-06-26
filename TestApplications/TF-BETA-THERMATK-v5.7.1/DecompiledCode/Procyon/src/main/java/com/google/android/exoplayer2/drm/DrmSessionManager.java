// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

import android.os.Looper;
import android.annotation.TargetApi;

@TargetApi(16)
public interface DrmSessionManager<T extends ExoMediaCrypto>
{
    DrmSession<T> acquireSession(final Looper p0, final DrmInitData p1);
    
    boolean canAcquireSession(final DrmInitData p0);
    
    void releaseSession(final DrmSession<T> p0);
}
