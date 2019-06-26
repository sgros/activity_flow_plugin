package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$pydpGfyEJY86wZVM-5gk1G5enLU */
public final /* synthetic */ class C0871-$$Lambda$MessagesStorage$pydpGfyEJY86wZVM-5gk1G5enLU implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C0871-$$Lambda$MessagesStorage$pydpGfyEJY86wZVM-5gk1G5enLU(MessagesStorage messagesStorage, int i, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = j;
    }

    public final void run() {
        this.f$0.lambda$setDialogPinned$147$MessagesStorage(this.f$1, this.f$2);
    }
}
