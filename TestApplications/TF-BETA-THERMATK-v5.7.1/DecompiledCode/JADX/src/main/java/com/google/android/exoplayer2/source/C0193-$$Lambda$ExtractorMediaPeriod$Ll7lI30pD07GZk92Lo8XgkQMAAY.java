package com.google.android.exoplayer2.source;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.source.-$$Lambda$ExtractorMediaPeriod$Ll7lI30pD07GZk92Lo8XgkQMAAY */
public final /* synthetic */ class C0193-$$Lambda$ExtractorMediaPeriod$Ll7lI30pD07GZk92Lo8XgkQMAAY implements Runnable {
    private final /* synthetic */ ExtractorMediaPeriod f$0;

    public /* synthetic */ C0193-$$Lambda$ExtractorMediaPeriod$Ll7lI30pD07GZk92Lo8XgkQMAAY(ExtractorMediaPeriod extractorMediaPeriod) {
        this.f$0 = extractorMediaPeriod;
    }

    public final void run() {
        this.f$0.maybeFinishPrepare();
    }
}
