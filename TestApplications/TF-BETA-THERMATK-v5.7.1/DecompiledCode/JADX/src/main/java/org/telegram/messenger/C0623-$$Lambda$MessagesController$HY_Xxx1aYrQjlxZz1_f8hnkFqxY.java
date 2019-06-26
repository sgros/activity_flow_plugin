package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.ChatFull;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$HY_Xxx1aYrQjlxZz1_f8hnkFqxY */
public final /* synthetic */ class C0623-$$Lambda$MessagesController$HY_Xxx1aYrQjlxZz1_f8hnkFqxY implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ ChatFull f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ C0623-$$Lambda$MessagesController$HY_Xxx1aYrQjlxZz1_f8hnkFqxY(MessagesController messagesController, ChatFull chatFull, String str) {
        this.f$0 = messagesController;
        this.f$1 = chatFull;
        this.f$2 = str;
    }

    public final void run() {
        this.f$0.lambda$null$172$MessagesController(this.f$1, this.f$2);
    }
}
