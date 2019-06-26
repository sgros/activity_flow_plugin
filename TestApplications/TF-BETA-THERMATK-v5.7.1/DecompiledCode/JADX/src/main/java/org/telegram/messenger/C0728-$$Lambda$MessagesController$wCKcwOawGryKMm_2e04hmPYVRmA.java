package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Updates;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$wCKcwOawGryKMm_2e04hmPYVRmA */
public final /* synthetic */ class C0728-$$Lambda$MessagesController$wCKcwOawGryKMm_2e04hmPYVRmA implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ Updates f$1;

    public /* synthetic */ C0728-$$Lambda$MessagesController$wCKcwOawGryKMm_2e04hmPYVRmA(MessagesController messagesController, Updates updates) {
        this.f$0 = messagesController;
        this.f$1 = updates;
    }

    public final void run() {
        this.f$0.lambda$null$159$MessagesController(this.f$1);
    }
}
