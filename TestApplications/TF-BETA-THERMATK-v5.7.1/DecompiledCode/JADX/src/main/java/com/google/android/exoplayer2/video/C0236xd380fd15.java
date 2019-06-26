package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.video.VideoRendererEventListener.EventDispatcher;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.video.-$$Lambda$VideoRendererEventListener$EventDispatcher$26y6c6BFFT4OL6bJiMmdsfxDEMQ */
public final /* synthetic */ class C0236xd380fd15 implements Runnable {
    private final /* synthetic */ EventDispatcher f$0;
    private final /* synthetic */ Format f$1;

    public /* synthetic */ C0236xd380fd15(EventDispatcher eventDispatcher, Format format) {
        this.f$0 = eventDispatcher;
        this.f$1 = format;
    }

    public final void run() {
        this.f$0.mo3819xe7570b3(this.f$1);
    }
}
