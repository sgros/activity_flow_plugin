package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.ChatFull;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$nxqnJkYq3vw5KhxgpTpnuTR0WR8 */
public final /* synthetic */ class C0868-$$Lambda$MessagesStorage$nxqnJkYq3vw5KhxgpTpnuTR0WR8 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ ChatFull f$1;

    public /* synthetic */ C0868-$$Lambda$MessagesStorage$nxqnJkYq3vw5KhxgpTpnuTR0WR8(MessagesStorage messagesStorage, ChatFull chatFull) {
        this.f$0 = messagesStorage;
        this.f$1 = chatFull;
    }

    public final void run() {
        this.f$0.lambda$null$66$MessagesStorage(this.f$1);
    }
}
