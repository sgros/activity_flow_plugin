package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$7jw738vBdzQ8x4ji7riWY5uMZu8 */
public final /* synthetic */ class C0762-$$Lambda$MessagesStorage$7jw738vBdzQ8x4ji7riWY5uMZu8 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ Object[] f$3;
    private final /* synthetic */ CountDownLatch f$4;

    public /* synthetic */ C0762-$$Lambda$MessagesStorage$7jw738vBdzQ8x4ji7riWY5uMZu8(MessagesStorage messagesStorage, String str, int i, Object[] objArr, CountDownLatch countDownLatch) {
        this.f$0 = messagesStorage;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = objArr;
        this.f$4 = countDownLatch;
    }

    public final void run() {
        this.f$0.lambda$getSentFile$102$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}