package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.audio.AudioRendererEventListener.EventDispatcher;
import com.google.android.exoplayer2.decoder.DecoderCounters;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.audio.-$$Lambda$AudioRendererEventListener$EventDispatcher$jb22FSnmUl2pGG0LguQS_Wd-LWk */
public final /* synthetic */ class C0143xbffc7ff8 implements Runnable {
    private final /* synthetic */ EventDispatcher f$0;
    private final /* synthetic */ DecoderCounters f$1;

    public /* synthetic */ C0143xbffc7ff8(EventDispatcher eventDispatcher, DecoderCounters decoderCounters) {
        this.f$0 = eventDispatcher;
        this.f$1 = decoderCounters;
    }

    public final void run() {
        this.f$0.lambda$disabled$4$AudioRendererEventListener$EventDispatcher(this.f$1);
    }
}
