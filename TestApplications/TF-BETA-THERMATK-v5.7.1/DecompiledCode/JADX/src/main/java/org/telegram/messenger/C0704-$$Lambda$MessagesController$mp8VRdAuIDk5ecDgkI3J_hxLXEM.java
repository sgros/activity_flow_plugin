package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$mp8VRdAuIDk5ecDgkI3J_hxLXEM */
public final /* synthetic */ class C0704-$$Lambda$MessagesController$mp8VRdAuIDk5ecDgkI3J_hxLXEM implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C0704-$$Lambda$MessagesController$mp8VRdAuIDk5ecDgkI3J_hxLXEM(MessagesController messagesController, TL_error tL_error, int i) {
        this.f$0 = messagesController;
        this.f$1 = tL_error;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$null$16$MessagesController(this.f$1, this.f$2);
    }
}
