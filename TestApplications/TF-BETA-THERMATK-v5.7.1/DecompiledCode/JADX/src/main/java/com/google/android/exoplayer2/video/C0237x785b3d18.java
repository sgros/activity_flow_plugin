package com.google.android.exoplayer2.video;

import android.view.Surface;
import com.google.android.exoplayer2.video.VideoRendererEventListener.EventDispatcher;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.video.-$$Lambda$VideoRendererEventListener$EventDispatcher$SFK5uUI0PHTm3Dg6Wdc1eRaQ9xk */
public final /* synthetic */ class C0237x785b3d18 implements Runnable {
    private final /* synthetic */ EventDispatcher f$0;
    private final /* synthetic */ Surface f$1;

    public /* synthetic */ C0237x785b3d18(EventDispatcher eventDispatcher, Surface surface) {
        this.f$0 = eventDispatcher;
        this.f$1 = surface;
    }

    public final void run() {
        this.f$0.mo3820x44bb7f11(this.f$1);
    }
}
