package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.ChatFull;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$OhB9rklUh_vaW5jSFjxvzZmL4rA */
public final /* synthetic */ class C0806-$$Lambda$MessagesStorage$OhB9rklUh_vaW5jSFjxvzZmL4rA implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ ChatFull f$1;

    public /* synthetic */ C0806-$$Lambda$MessagesStorage$OhB9rklUh_vaW5jSFjxvzZmL4rA(MessagesStorage messagesStorage, ChatFull chatFull) {
        this.f$0 = messagesStorage;
        this.f$1 = chatFull;
    }

    public final void run() {
        this.f$0.lambda$null$81$MessagesStorage(this.f$1);
    }
}
