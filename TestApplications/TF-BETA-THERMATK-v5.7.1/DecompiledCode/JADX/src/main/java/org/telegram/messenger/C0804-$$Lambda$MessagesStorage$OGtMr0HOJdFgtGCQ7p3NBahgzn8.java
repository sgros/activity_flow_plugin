package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$OGtMr0HOJdFgtGCQ7p3NBahgzn8 */
public final /* synthetic */ class C0804-$$Lambda$MessagesStorage$OGtMr0HOJdFgtGCQ7p3NBahgzn8 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ User[] f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ CountDownLatch f$3;

    public /* synthetic */ C0804-$$Lambda$MessagesStorage$OGtMr0HOJdFgtGCQ7p3NBahgzn8(MessagesStorage messagesStorage, User[] userArr, int i, CountDownLatch countDownLatch) {
        this.f$0 = messagesStorage;
        this.f$1 = userArr;
        this.f$2 = i;
        this.f$3 = countDownLatch;
    }

    public final void run() {
        this.f$0.lambda$getUserSync$151$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}
