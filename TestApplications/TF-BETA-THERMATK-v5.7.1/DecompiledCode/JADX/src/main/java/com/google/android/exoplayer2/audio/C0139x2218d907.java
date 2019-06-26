package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioRendererEventListener.EventDispatcher;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.audio.-$$Lambda$AudioRendererEventListener$EventDispatcher$D7KvJbrpXrnWw4qzd_LI9ZtQytw */
public final /* synthetic */ class C0139x2218d907 implements Runnable {
    private final /* synthetic */ EventDispatcher f$0;
    private final /* synthetic */ Format f$1;

    public /* synthetic */ C0139x2218d907(EventDispatcher eventDispatcher, Format format) {
        this.f$0 = eventDispatcher;
        this.f$1 = format;
    }

    public final void run() {
        this.f$0.mo2699x2eadf638(this.f$1);
    }
}
