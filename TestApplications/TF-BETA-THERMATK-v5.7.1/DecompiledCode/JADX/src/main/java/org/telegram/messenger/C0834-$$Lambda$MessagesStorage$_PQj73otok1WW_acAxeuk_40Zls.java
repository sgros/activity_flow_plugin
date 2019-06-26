package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$_PQj73otok1WW_acAxeuk_40Zls */
public final /* synthetic */ class C0834-$$Lambda$MessagesStorage$_PQj73otok1WW_acAxeuk_40Zls implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C0834-$$Lambda$MessagesStorage$_PQj73otok1WW_acAxeuk_40Zls(MessagesStorage messagesStorage, int i, int i2) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run() {
        this.f$0.lambda$deleteUserChannelHistory$43$MessagesStorage(this.f$1, this.f$2);
    }
}
