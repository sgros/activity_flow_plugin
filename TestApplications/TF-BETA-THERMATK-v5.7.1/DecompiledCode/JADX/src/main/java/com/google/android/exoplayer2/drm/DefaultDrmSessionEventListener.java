package com.google.android.exoplayer2.drm;

public interface DefaultDrmSessionEventListener {
    void onDrmKeysLoaded();

    void onDrmKeysRestored();

    void onDrmSessionAcquired();

    void onDrmSessionManagerError(Exception exception);

    void onDrmSessionReleased();
}
