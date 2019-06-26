// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.drm;

import java.util.Map;
import com.google.android.exoplayer2.util.Assertions;

public final class ErrorStateDrmSession<T extends ExoMediaCrypto> implements DrmSession<T>
{
    private final DrmSessionException error;
    
    public ErrorStateDrmSession(final DrmSessionException ex) {
        Assertions.checkNotNull(ex);
        this.error = ex;
    }
    
    @Override
    public DrmSessionException getError() {
        return this.error;
    }
    
    @Override
    public T getMediaCrypto() {
        return null;
    }
    
    @Override
    public int getState() {
        return 1;
    }
    
    @Override
    public Map<String, String> queryKeyStatus() {
        return null;
    }
}
