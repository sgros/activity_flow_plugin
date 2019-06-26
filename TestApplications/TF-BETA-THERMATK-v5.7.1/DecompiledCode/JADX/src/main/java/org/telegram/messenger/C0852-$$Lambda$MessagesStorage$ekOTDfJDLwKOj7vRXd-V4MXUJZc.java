package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$ekOTDfJDLwKOj7vRXd-V4MXUJZc */
public final /* synthetic */ class C0852-$$Lambda$MessagesStorage$ekOTDfJDLwKOj7vRXd-V4MXUJZc implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ C0852-$$Lambda$MessagesStorage$ekOTDfJDLwKOj7vRXd-V4MXUJZc(MessagesStorage messagesStorage, boolean z) {
        this.f$0 = messagesStorage;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.lambda$cleanup$3$MessagesStorage(this.f$1);
    }
}
