package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$0TaEdYhzl8U-O7oEChUGdj3lNyw */
public final /* synthetic */ class C0742-$$Lambda$MessagesStorage$0TaEdYhzl8U-O7oEChUGdj3lNyw implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0742-$$Lambda$MessagesStorage$0TaEdYhzl8U-O7oEChUGdj3lNyw(MessagesStorage messagesStorage, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$deleteBlockedUser$39$MessagesStorage(this.f$1);
    }
}
