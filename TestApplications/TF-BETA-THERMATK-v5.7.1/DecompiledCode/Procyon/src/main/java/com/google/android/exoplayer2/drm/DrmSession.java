// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

import java.util.Map;
import android.annotation.TargetApi;

@TargetApi(16)
public interface DrmSession<T extends ExoMediaCrypto>
{
    DrmSessionException getError();
    
    T getMediaCrypto();
    
    int getState();
    
    Map<String, String> queryKeyStatus();
    
    public static class DrmSessionException extends Exception
    {
        public DrmSessionException(final Throwable cause) {
            super(cause);
        }
    }
}
