package com.google.android.exoplayer2;

import com.google.android.exoplayer2.Player.EventListener;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.-$$Lambda$ExoPlayerImpl$jeRtn5zzqb8T3nNL82wu8yFBJNo */
public final /* synthetic */ class C3323-$$Lambda$ExoPlayerImpl$jeRtn5zzqb8T3nNL82wu8yFBJNo implements ListenerInvocation {
    private final /* synthetic */ ExoPlaybackException f$0;

    public /* synthetic */ C3323-$$Lambda$ExoPlayerImpl$jeRtn5zzqb8T3nNL82wu8yFBJNo(ExoPlaybackException exoPlaybackException) {
        this.f$0 = exoPlaybackException;
    }

    public final void invokeListener(EventListener eventListener) {
        eventListener.onPlayerError(this.f$0);
    }
}
