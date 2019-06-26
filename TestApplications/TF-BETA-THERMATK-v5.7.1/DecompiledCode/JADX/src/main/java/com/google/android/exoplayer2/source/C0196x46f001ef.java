package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.MediaSourceEventListener.MediaLoadData;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.source.-$$Lambda$MediaSourceEventListener$EventDispatcher$BtPa14lQQTv1oUeMy_9QaCysWHY */
public final /* synthetic */ class C0196x46f001ef implements Runnable {
    private final /* synthetic */ EventDispatcher f$0;
    private final /* synthetic */ MediaSourceEventListener f$1;
    private final /* synthetic */ MediaPeriodId f$2;
    private final /* synthetic */ MediaLoadData f$3;

    public /* synthetic */ C0196x46f001ef(EventDispatcher eventDispatcher, MediaSourceEventListener mediaSourceEventListener, MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
        this.f$0 = eventDispatcher;
        this.f$1 = mediaSourceEventListener;
        this.f$2 = mediaPeriodId;
        this.f$3 = mediaLoadData;
    }

    public final void run() {
        this.f$0.mo3151xcce9218(this.f$1, this.f$2, this.f$3);
    }
}
