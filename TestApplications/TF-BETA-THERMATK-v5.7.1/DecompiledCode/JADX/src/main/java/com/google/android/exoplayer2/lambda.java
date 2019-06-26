package com.google.android.exoplayer2;

import java.util.concurrent.CopyOnWriteArrayList;

/* renamed from: com.google.android.exoplayer2.-$$Lambda$ExoPlayerImpl$DrcaME6RvvSdC72wmoYPUB4uP5w */
public final /* synthetic */ class lambda implements Runnable {
    private final /* synthetic */ CopyOnWriteArrayList f$0;
    private final /* synthetic */ ListenerInvocation f$1;

    public /* synthetic */ lambda(CopyOnWriteArrayList copyOnWriteArrayList, ListenerInvocation listenerInvocation) {
        this.f$0 = copyOnWriteArrayList;
        this.f$1 = listenerInvocation;
    }

    public final void run() {
        ExoPlayerImpl.invokeAll(this.f$0, this.f$1);
    }
}
