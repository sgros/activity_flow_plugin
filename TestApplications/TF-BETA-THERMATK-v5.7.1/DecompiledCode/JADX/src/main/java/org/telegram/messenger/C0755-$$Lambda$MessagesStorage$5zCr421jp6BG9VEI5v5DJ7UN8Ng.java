package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$5zCr421jp6BG9VEI5v5DJ7UN8Ng */
public final /* synthetic */ class C0755-$$Lambda$MessagesStorage$5zCr421jp6BG9VEI5v5DJ7UN8Ng implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ Integer[] f$2;
    private final /* synthetic */ CountDownLatch f$3;

    public /* synthetic */ C0755-$$Lambda$MessagesStorage$5zCr421jp6BG9VEI5v5DJ7UN8Ng(MessagesStorage messagesStorage, int i, Integer[] numArr, CountDownLatch countDownLatch) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = numArr;
        this.f$3 = countDownLatch;
    }

    public final void run() {
        this.f$0.lambda$getChannelPtsSync$150$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}
