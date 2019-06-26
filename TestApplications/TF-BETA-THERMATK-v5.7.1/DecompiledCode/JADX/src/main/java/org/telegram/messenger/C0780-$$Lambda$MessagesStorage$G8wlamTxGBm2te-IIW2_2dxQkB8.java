package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$G8wlamTxGBm2te-IIW2_2dxQkB8 */
public final /* synthetic */ class C0780-$$Lambda$MessagesStorage$G8wlamTxGBm2te-IIW2_2dxQkB8 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C0780-$$Lambda$MessagesStorage$G8wlamTxGBm2te-IIW2_2dxQkB8(MessagesStorage messagesStorage, int i, int i2) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run() {
        this.f$0.lambda$markMessagesAsDeleted$135$MessagesStorage(this.f$1, this.f$2);
    }
}
