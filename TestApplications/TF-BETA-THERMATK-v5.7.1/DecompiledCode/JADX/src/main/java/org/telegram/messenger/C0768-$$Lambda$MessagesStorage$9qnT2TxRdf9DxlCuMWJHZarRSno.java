package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$9qnT2TxRdf9DxlCuMWJHZarRSno */
public final /* synthetic */ class C0768-$$Lambda$MessagesStorage$9qnT2TxRdf9DxlCuMWJHZarRSno implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ C0768-$$Lambda$MessagesStorage$9qnT2TxRdf9DxlCuMWJHZarRSno(MessagesStorage messagesStorage, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
    }

    public final void run() {
        this.f$0.lambda$onDeleteQueryComplete$46$MessagesStorage(this.f$1);
    }
}
