package org.telegram.messenger;

import org.telegram.messenger.MessagesStorage.IntCallback;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$-im1wcrbOwH0G5dj6QGJ2KPPUzg */
public final /* synthetic */ class C0740-$$Lambda$MessagesStorage$-im1wcrbOwH0G5dj6QGJ2KPPUzg implements Runnable {
    private final /* synthetic */ IntCallback f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0740-$$Lambda$MessagesStorage$-im1wcrbOwH0G5dj6QGJ2KPPUzg(IntCallback intCallback, int i) {
        this.f$0 = intCallback;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.run(this.f$1);
    }
}
