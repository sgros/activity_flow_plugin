package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$EDTDSO5ZD7EJWhgiA72HtAyXwnM */
public final /* synthetic */ class C0777-$$Lambda$MessagesStorage$EDTDSO5ZD7EJWhgiA72HtAyXwnM implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0777-$$Lambda$MessagesStorage$EDTDSO5ZD7EJWhgiA72HtAyXwnM(MessagesStorage messagesStorage, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$putMessagesInternal$123$MessagesStorage(this.f$1);
    }
}
