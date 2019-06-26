package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.DrmSession.DrmSessionException;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Map;

public final class ErrorStateDrmSession<T extends ExoMediaCrypto> implements DrmSession<T> {
    private final DrmSessionException error;

    public T getMediaCrypto() {
        return null;
    }

    public int getState() {
        return 1;
    }

    public Map<String, String> queryKeyStatus() {
        return null;
    }

    public ErrorStateDrmSession(DrmSessionException drmSessionException) {
        Assertions.checkNotNull(drmSessionException);
        this.error = drmSessionException;
    }

    public DrmSessionException getError() {
        return this.error;
    }
}
