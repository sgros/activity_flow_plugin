package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.ChatFull;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$d0gfmXVoBhLfPpsJm2PNGRtagwc */
public final /* synthetic */ class C0848-$$Lambda$MessagesStorage$d0gfmXVoBhLfPpsJm2PNGRtagwc implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ ChatFull f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ C0848-$$Lambda$MessagesStorage$d0gfmXVoBhLfPpsJm2PNGRtagwc(MessagesStorage messagesStorage, ChatFull chatFull, boolean z) {
        this.f$0 = messagesStorage;
        this.f$1 = chatFull;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$updateChatInfo$75$MessagesStorage(this.f$1, this.f$2);
    }
}
