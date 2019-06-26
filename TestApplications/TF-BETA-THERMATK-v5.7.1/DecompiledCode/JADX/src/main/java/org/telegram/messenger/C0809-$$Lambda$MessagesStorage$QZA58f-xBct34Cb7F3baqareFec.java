package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$QZA58f-xBct34Cb7F3baqareFec */
public final /* synthetic */ class C0809-$$Lambda$MessagesStorage$QZA58f-xBct34Cb7F3baqareFec implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C0809-$$Lambda$MessagesStorage$QZA58f-xBct34Cb7F3baqareFec(MessagesStorage messagesStorage, int i, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = j;
    }

    public final void run() {
        this.f$0.lambda$resetMentionsCount$60$MessagesStorage(this.f$1, this.f$2);
    }
}
