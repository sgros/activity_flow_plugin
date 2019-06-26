package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$g1iuRG0K-i5oYkMy9HCpNOf8FqU */
public final /* synthetic */ class C0854-$$Lambda$MessagesStorage$g1iuRG0K-i5oYkMy9HCpNOf8FqU implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C0854-$$Lambda$MessagesStorage$g1iuRG0K-i5oYkMy9HCpNOf8FqU(MessagesStorage messagesStorage, int i, int i2) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run() {
        this.f$0.lambda$updateUserPinnedMessage$77$MessagesStorage(this.f$1, this.f$2);
    }
}
