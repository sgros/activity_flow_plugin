package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$gHn5_xuiEvlIe5KWph9-QlfcuBk */
public final /* synthetic */ class C0855-$$Lambda$MessagesStorage$gHn5_xuiEvlIe5KWph9-QlfcuBk implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C0855-$$Lambda$MessagesStorage$gHn5_xuiEvlIe5KWph9-QlfcuBk(MessagesStorage messagesStorage, int i, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = j;
    }

    public final void run() {
        this.f$0.lambda$deleteDialog$45$MessagesStorage(this.f$1, this.f$2);
    }
}
