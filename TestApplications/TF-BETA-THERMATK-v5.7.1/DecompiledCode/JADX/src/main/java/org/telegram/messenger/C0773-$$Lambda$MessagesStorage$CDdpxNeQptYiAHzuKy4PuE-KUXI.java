package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Message;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$CDdpxNeQptYiAHzuKy4PuE-KUXI */
public final /* synthetic */ class C0773-$$Lambda$MessagesStorage$CDdpxNeQptYiAHzuKy4PuE-KUXI implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ Message f$1;

    public /* synthetic */ C0773-$$Lambda$MessagesStorage$CDdpxNeQptYiAHzuKy4PuE-KUXI(MessagesStorage messagesStorage, Message message) {
        this.f$0 = messagesStorage;
        this.f$1 = message;
    }

    public final void run() {
        this.f$0.lambda$markMessageAsSendError$125$MessagesStorage(this.f$1);
    }
}
