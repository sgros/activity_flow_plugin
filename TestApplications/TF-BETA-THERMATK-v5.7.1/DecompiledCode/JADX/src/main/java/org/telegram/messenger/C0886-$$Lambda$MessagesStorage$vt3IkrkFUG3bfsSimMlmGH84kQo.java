package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$vt3IkrkFUG3bfsSimMlmGH84kQo */
public final /* synthetic */ class C0886-$$Lambda$MessagesStorage$vt3IkrkFUG3bfsSimMlmGH84kQo implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ boolean[] f$2;
    private final /* synthetic */ CountDownLatch f$3;

    public /* synthetic */ C0886-$$Lambda$MessagesStorage$vt3IkrkFUG3bfsSimMlmGH84kQo(MessagesStorage messagesStorage, int i, boolean[] zArr, CountDownLatch countDownLatch) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = zArr;
        this.f$3 = countDownLatch;
    }

    public final void run() {
        this.f$0.lambda$isMigratedChat$83$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}
