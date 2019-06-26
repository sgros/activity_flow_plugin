package com.google.android.exoplayer2.source.smoothstreaming;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.source.smoothstreaming.-$$Lambda$SsMediaSource$tFjHmMdOxDkhvkY7QhPdfdPmbtI */
public final /* synthetic */ class C0212-$$Lambda$SsMediaSource$tFjHmMdOxDkhvkY7QhPdfdPmbtI implements Runnable {
    private final /* synthetic */ SsMediaSource f$0;

    public /* synthetic */ C0212-$$Lambda$SsMediaSource$tFjHmMdOxDkhvkY7QhPdfdPmbtI(SsMediaSource ssMediaSource) {
        this.f$0 = ssMediaSource;
    }

    public final void run() {
        this.f$0.startLoadingManifest();
    }
}
