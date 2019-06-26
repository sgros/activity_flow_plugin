package com.google.android.exoplayer2;

import com.google.android.exoplayer2.Player.EventListener;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.-$$Lambda$ExoPlayerImpl$PGMSl1-IXjPb8QR_4ohCB7W_Kv8 */
public final /* synthetic */ class C3317-$$Lambda$ExoPlayerImpl$PGMSl1-IXjPb8QR_4ohCB7W_Kv8 implements ListenerInvocation {
    private final /* synthetic */ PlaybackParameters f$0;

    public /* synthetic */ C3317-$$Lambda$ExoPlayerImpl$PGMSl1-IXjPb8QR_4ohCB7W_Kv8(PlaybackParameters playbackParameters) {
        this.f$0 = playbackParameters;
    }

    public final void invokeListener(EventListener eventListener) {
        eventListener.onPlaybackParametersChanged(this.f$0);
    }
}
