package org.telegram.messenger;

import org.telegram.messenger.MessagesStorage.IntCallback;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$u4y7AHI70oFXeY4qAMVbfOCvhKs */
public final /* synthetic */ class C0877-$$Lambda$MessagesStorage$u4y7AHI70oFXeY4qAMVbfOCvhKs implements Runnable {
    private final /* synthetic */ IntCallback f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0877-$$Lambda$MessagesStorage$u4y7AHI70oFXeY4qAMVbfOCvhKs(IntCallback intCallback, int i) {
        this.f$0 = intCallback;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.run(this.f$1);
    }
}
