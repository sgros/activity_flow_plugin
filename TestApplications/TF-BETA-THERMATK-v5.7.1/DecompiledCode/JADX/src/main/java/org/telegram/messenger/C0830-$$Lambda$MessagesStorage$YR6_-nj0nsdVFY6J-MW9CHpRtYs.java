package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$YR6_-nj0nsdVFY6J-MW9CHpRtYs */
public final /* synthetic */ class C0830-$$Lambda$MessagesStorage$YR6_-nj0nsdVFY6J-MW9CHpRtYs implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ C0830-$$Lambda$MessagesStorage$YR6_-nj0nsdVFY6J-MW9CHpRtYs(MessagesStorage messagesStorage, long j, boolean z) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$setDialogUnread$146$MessagesStorage(this.f$1, this.f$2);
    }
}
