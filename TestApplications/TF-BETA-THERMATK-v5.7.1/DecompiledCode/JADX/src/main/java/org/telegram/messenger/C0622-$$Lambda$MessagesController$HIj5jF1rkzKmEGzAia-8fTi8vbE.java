package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$HIj5jF1rkzKmEGzAia-8fTi8vbE */
public final /* synthetic */ class C0622-$$Lambda$MessagesController$HIj5jF1rkzKmEGzAia-8fTi8vbE implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ User f$1;

    public /* synthetic */ C0622-$$Lambda$MessagesController$HIj5jF1rkzKmEGzAia-8fTi8vbE(MessagesController messagesController, User user) {
        this.f$0 = messagesController;
        this.f$1 = user;
    }

    public final void run() {
        this.f$0.lambda$null$19$MessagesController(this.f$1);
    }
}
