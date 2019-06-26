package com.google.android.exoplayer2;

import com.google.android.exoplayer2.Player.EventListener;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.-$$Lambda$ExoPlayerImpl$OKMPvkXpqXeKaJZFBZ8m9YfNXpE */
public final /* synthetic */ class C3315-$$Lambda$ExoPlayerImpl$OKMPvkXpqXeKaJZFBZ8m9YfNXpE implements ListenerInvocation {
    private final /* synthetic */ boolean f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C3315-$$Lambda$ExoPlayerImpl$OKMPvkXpqXeKaJZFBZ8m9YfNXpE(boolean z, int i) {
        this.f$0 = z;
        this.f$1 = i;
    }

    public final void invokeListener(EventListener eventListener) {
        eventListener.onPlayerStateChanged(this.f$0, this.f$1);
    }
}
