package com.google.android.exoplayer2.source.dash;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.source.dash.-$$Lambda$DashMediaSource$QbzYvqCY1TT8f0KClkalovG-Oxc */
public final /* synthetic */ class C0206-$$Lambda$DashMediaSource$QbzYvqCY1TT8f0KClkalovG-Oxc implements Runnable {
    private final /* synthetic */ DashMediaSource f$0;

    public /* synthetic */ C0206-$$Lambda$DashMediaSource$QbzYvqCY1TT8f0KClkalovG-Oxc(DashMediaSource dashMediaSource) {
        this.f$0 = dashMediaSource;
    }

    public final void run() {
        this.f$0.startLoadingManifest();
    }
}
