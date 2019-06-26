package com.google.android.exoplayer2;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.-$$Lambda$ExoPlayerImplInternal$XwFxncwlyfAWA4k618O8BNtCsr0 */
public final /* synthetic */ class C0129-$$Lambda$ExoPlayerImplInternal$XwFxncwlyfAWA4k618O8BNtCsr0 implements Runnable {
    private final /* synthetic */ ExoPlayerImplInternal f$0;
    private final /* synthetic */ PlayerMessage f$1;

    public /* synthetic */ C0129-$$Lambda$ExoPlayerImplInternal$XwFxncwlyfAWA4k618O8BNtCsr0(ExoPlayerImplInternal exoPlayerImplInternal, PlayerMessage playerMessage) {
        this.f$0 = exoPlayerImplInternal;
        this.f$1 = playerMessage;
    }

    public final void run() {
        this.f$0.lambda$sendMessageToTargetThread$0$ExoPlayerImplInternal(this.f$1);
    }
}
