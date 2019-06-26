package com.google.android.exoplayer2;

import java.io.IOException;

public final class ExoPlaybackException extends Exception {
    private final Throwable cause;
    public final int rendererIndex;
    public final int type;

    public static ExoPlaybackException createForSource(IOException iOException) {
        return new ExoPlaybackException(0, iOException, -1);
    }

    public static ExoPlaybackException createForRenderer(Exception exception, int i) {
        return new ExoPlaybackException(1, exception, i);
    }

    static ExoPlaybackException createForUnexpected(RuntimeException runtimeException) {
        return new ExoPlaybackException(2, runtimeException, -1);
    }

    private ExoPlaybackException(int i, Throwable th, int i2) {
        super(th);
        this.type = i;
        this.cause = th;
        this.rendererIndex = i2;
    }
}
