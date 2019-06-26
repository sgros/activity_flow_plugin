package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$G2AR1YES2RX33l0g8yjj7MWOmwU */
public final /* synthetic */ class C0779-$$Lambda$MessagesStorage$G2AR1YES2RX33l0g8yjj7MWOmwU implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ C0779-$$Lambda$MessagesStorage$G2AR1YES2RX33l0g8yjj7MWOmwU(MessagesStorage messagesStorage, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
    }

    public final void run() {
        this.f$0.lambda$markMessageAsMention$59$MessagesStorage(this.f$1);
    }
}
