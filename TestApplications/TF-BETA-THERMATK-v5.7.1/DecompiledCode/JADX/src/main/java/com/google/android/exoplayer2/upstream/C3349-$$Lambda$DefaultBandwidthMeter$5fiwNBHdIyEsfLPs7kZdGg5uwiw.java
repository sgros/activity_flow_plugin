package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.BandwidthMeter.EventListener;
import com.google.android.exoplayer2.util.EventDispatcher.Event;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.upstream.-$$Lambda$DefaultBandwidthMeter$5fiwNBHdIyEsfLPs7kZdGg5uwiw */
public final /* synthetic */ class C3349-$$Lambda$DefaultBandwidthMeter$5fiwNBHdIyEsfLPs7kZdGg5uwiw implements Event {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C3349-$$Lambda$DefaultBandwidthMeter$5fiwNBHdIyEsfLPs7kZdGg5uwiw(int i, long j, long j2) {
        this.f$0 = i;
        this.f$1 = j;
        this.f$2 = j2;
    }

    public final void sendTo(Object obj) {
        ((EventListener) obj).onBandwidthSample(this.f$0, this.f$1, this.f$2);
    }
}
