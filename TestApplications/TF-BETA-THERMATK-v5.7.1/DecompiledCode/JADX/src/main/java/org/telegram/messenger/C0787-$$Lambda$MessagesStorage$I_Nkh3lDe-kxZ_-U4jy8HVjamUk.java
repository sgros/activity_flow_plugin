package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$I_Nkh3lDe-kxZ_-U4jy8HVjamUk */
public final /* synthetic */ class C0787-$$Lambda$MessagesStorage$I_Nkh3lDe-kxZ_-U4jy8HVjamUk implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C0787-$$Lambda$MessagesStorage$I_Nkh3lDe-kxZ_-U4jy8HVjamUk(MessagesStorage messagesStorage, int i, int i2, int i3) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
    }

    public final void run() {
        this.f$0.lambda$setMessageSeq$126$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}
