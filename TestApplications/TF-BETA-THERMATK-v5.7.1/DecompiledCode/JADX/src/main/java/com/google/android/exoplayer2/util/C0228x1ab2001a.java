package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.util.EventDispatcher.Event;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.util.-$$Lambda$EventDispatcher$HandlerAndListener$uD_JKgYUi0f_RBL7K02WSc4AoE4 */
public final /* synthetic */ class C0228x1ab2001a implements Runnable {
    private final /* synthetic */ HandlerAndListener f$0;
    private final /* synthetic */ Event f$1;

    public /* synthetic */ C0228x1ab2001a(HandlerAndListener handlerAndListener, Event event) {
        this.f$0 = handlerAndListener;
        this.f$1 = event;
    }

    public final void run() {
        this.f$0.lambda$dispatch$0$EventDispatcher$HandlerAndListener(this.f$1);
    }
}
