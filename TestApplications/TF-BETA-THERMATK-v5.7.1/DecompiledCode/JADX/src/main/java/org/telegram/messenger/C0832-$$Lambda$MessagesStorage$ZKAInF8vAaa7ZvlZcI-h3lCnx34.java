package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$ZKAInF8vAaa7ZvlZcI-h3lCnx34 */
public final /* synthetic */ class C0832-$$Lambda$MessagesStorage$ZKAInF8vAaa7ZvlZcI-h3lCnx34 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C0832-$$Lambda$MessagesStorage$ZKAInF8vAaa7ZvlZcI-h3lCnx34(MessagesStorage messagesStorage, int i, int i2) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run() {
        this.f$0.lambda$updateChatPinnedMessage$80$MessagesStorage(this.f$1, this.f$2);
    }
}
