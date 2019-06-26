package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.updates_Difference;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$T4klBi4vmBoUx4jRc3cN4SM0ygE */
public final /* synthetic */ class C0657-$$Lambda$MessagesController$T4klBi4vmBoUx4jRc3cN4SM0ygE implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ updates_Difference f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C0657-$$Lambda$MessagesController$T4klBi4vmBoUx4jRc3cN4SM0ygE(MessagesController messagesController, updates_Difference updates_difference, int i, int i2) {
        this.f$0 = messagesController;
        this.f$1 = updates_difference;
        this.f$2 = i;
        this.f$3 = i2;
    }

    public final void run() {
        this.f$0.lambda$null$206$MessagesController(this.f$1, this.f$2, this.f$3);
    }
}
