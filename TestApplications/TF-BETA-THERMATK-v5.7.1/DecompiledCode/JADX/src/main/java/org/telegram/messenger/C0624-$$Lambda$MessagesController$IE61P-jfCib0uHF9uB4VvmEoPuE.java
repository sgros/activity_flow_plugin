package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_updateUserBlocked;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$IE61P-jfCib0uHF9uB4VvmEoPuE */
public final /* synthetic */ class C0624-$$Lambda$MessagesController$IE61P-jfCib0uHF9uB4VvmEoPuE implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ TL_updateUserBlocked f$1;

    public /* synthetic */ C0624-$$Lambda$MessagesController$IE61P-jfCib0uHF9uB4VvmEoPuE(MessagesController messagesController, TL_updateUserBlocked tL_updateUserBlocked) {
        this.f$0 = messagesController;
        this.f$1 = tL_updateUserBlocked;
    }

    public final void run() {
        this.f$0.lambda$null$240$MessagesController(this.f$1);
    }
}
