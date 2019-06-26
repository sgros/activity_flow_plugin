package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_chatOnlines;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$OmsxlAaybMmJyhzcSO6qE2vdkAg */
public final /* synthetic */ class C0649-$$Lambda$MessagesController$OmsxlAaybMmJyhzcSO6qE2vdkAg implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ TL_chatOnlines f$2;

    public /* synthetic */ C0649-$$Lambda$MessagesController$OmsxlAaybMmJyhzcSO6qE2vdkAg(MessagesController messagesController, int i, TL_chatOnlines tL_chatOnlines) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = tL_chatOnlines;
    }

    public final void run() {
        this.f$0.lambda$null$84$MessagesController(this.f$1, this.f$2);
    }
}
