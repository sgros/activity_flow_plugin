package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_config;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$E_B3T1gXoq4eD739GgHdBDLPpBs */
public final /* synthetic */ class C0611-$$Lambda$MessagesController$E_B3T1gXoq4eD739GgHdBDLPpBs implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ TL_config f$1;

    public /* synthetic */ C0611-$$Lambda$MessagesController$E_B3T1gXoq4eD739GgHdBDLPpBs(MessagesController messagesController, TL_config tL_config) {
        this.f$0 = messagesController;
        this.f$1 = tL_config;
    }

    public final void run() {
        this.f$0.lambda$updateConfig$2$MessagesController(this.f$1);
    }
}
