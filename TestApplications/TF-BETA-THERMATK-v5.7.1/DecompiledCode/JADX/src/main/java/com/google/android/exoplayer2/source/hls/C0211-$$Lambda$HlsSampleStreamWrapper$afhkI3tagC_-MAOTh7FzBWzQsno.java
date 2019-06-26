package com.google.android.exoplayer2.source.hls;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.source.hls.-$$Lambda$HlsSampleStreamWrapper$afhkI3tagC_-MAOTh7FzBWzQsno */
public final /* synthetic */ class C0211-$$Lambda$HlsSampleStreamWrapper$afhkI3tagC_-MAOTh7FzBWzQsno implements Runnable {
    private final /* synthetic */ HlsSampleStreamWrapper f$0;

    public /* synthetic */ C0211-$$Lambda$HlsSampleStreamWrapper$afhkI3tagC_-MAOTh7FzBWzQsno(HlsSampleStreamWrapper hlsSampleStreamWrapper) {
        this.f$0 = hlsSampleStreamWrapper;
    }

    public final void run() {
        this.f$0.onTracksEnded();
    }
}
