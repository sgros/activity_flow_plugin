package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$d1ma4ral7Pvbl0aQnDGt6um4wvc */
public final /* synthetic */ class C0849-$$Lambda$MessagesStorage$d1ma4ral7Pvbl0aQnDGt6um4wvc implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ boolean[] f$2;
    private final /* synthetic */ CountDownLatch f$3;

    public /* synthetic */ C0849-$$Lambda$MessagesStorage$d1ma4ral7Pvbl0aQnDGt6um4wvc(MessagesStorage messagesStorage, long j, boolean[] zArr, CountDownLatch countDownLatch) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = zArr;
        this.f$3 = countDownLatch;
    }

    public final void run() {
        this.f$0.lambda$checkMessageByRandomId$93$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}
