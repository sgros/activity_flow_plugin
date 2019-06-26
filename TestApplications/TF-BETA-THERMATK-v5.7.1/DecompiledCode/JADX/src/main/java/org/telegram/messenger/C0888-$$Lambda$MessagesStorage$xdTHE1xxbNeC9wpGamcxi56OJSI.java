package org.telegram.messenger;

import org.telegram.messenger.MessagesStorage.IntCallback;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$xdTHE1xxbNeC9wpGamcxi56OJSI */
public final /* synthetic */ class C0888-$$Lambda$MessagesStorage$xdTHE1xxbNeC9wpGamcxi56OJSI implements Runnable {
    private final /* synthetic */ IntCallback f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0888-$$Lambda$MessagesStorage$xdTHE1xxbNeC9wpGamcxi56OJSI(IntCallback intCallback, int i) {
        this.f$0 = intCallback;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.run(this.f$1);
    }
}
