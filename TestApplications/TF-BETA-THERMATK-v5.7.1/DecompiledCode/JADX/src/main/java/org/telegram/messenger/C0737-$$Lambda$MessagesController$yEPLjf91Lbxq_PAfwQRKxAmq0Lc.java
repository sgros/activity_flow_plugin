package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_messages_chatFull;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$yEPLjf91Lbxq_PAfwQRKxAmq0Lc */
public final /* synthetic */ class C0737-$$Lambda$MessagesController$yEPLjf91Lbxq_PAfwQRKxAmq0Lc implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ TL_messages_chatFull f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C0737-$$Lambda$MessagesController$yEPLjf91Lbxq_PAfwQRKxAmq0Lc(MessagesController messagesController, int i, TL_messages_chatFull tL_messages_chatFull, int i2) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = tL_messages_chatFull;
        this.f$3 = i2;
    }

    public final void run() {
        this.f$0.lambda$null$15$MessagesController(this.f$1, this.f$2, this.f$3);
    }
}
