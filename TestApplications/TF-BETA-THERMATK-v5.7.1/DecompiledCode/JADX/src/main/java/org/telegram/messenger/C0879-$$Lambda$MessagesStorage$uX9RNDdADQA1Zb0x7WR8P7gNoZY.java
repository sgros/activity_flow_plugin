package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$uX9RNDdADQA1Zb0x7WR8P7gNoZY */
public final /* synthetic */ class C0879-$$Lambda$MessagesStorage$uX9RNDdADQA1Zb0x7WR8P7gNoZY implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ long f$3;

    public /* synthetic */ C0879-$$Lambda$MessagesStorage$uX9RNDdADQA1Zb0x7WR8P7gNoZY(MessagesStorage messagesStorage, boolean z, int i, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = z;
        this.f$2 = i;
        this.f$3 = j;
    }

    public final void run() {
        this.f$0.lambda$removeFromDownloadQueue$114$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}