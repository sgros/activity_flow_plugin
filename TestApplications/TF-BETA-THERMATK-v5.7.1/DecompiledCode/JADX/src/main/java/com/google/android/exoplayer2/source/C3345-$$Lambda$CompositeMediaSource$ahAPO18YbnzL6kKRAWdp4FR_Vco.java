package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource.SourceInfoRefreshListener;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.source.-$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco */
public final /* synthetic */ class C3345-$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco implements SourceInfoRefreshListener {
    private final /* synthetic */ CompositeMediaSource f$0;
    private final /* synthetic */ Object f$1;

    public /* synthetic */ C3345-$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco(CompositeMediaSource compositeMediaSource, Object obj) {
        this.f$0 = compositeMediaSource;
        this.f$1 = obj;
    }

    public final void onSourceInfoRefreshed(MediaSource mediaSource, Timeline timeline, Object obj) {
        this.f$0.lambda$prepareChildSource$0$CompositeMediaSource(this.f$1, mediaSource, timeline, obj);
    }
}
