package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Chat;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$GABj5DmLp4L1Zi8c9sUorNKjQvk */
public final /* synthetic */ class C0617-$$Lambda$MessagesController$GABj5DmLp4L1Zi8c9sUorNKjQvk implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ Chat f$1;

    public /* synthetic */ C0617-$$Lambda$MessagesController$GABj5DmLp4L1Zi8c9sUorNKjQvk(MessagesController messagesController, Chat chat) {
        this.f$0 = messagesController;
        this.f$1 = chat;
    }

    public final void run() {
        this.f$0.lambda$null$248$MessagesController(this.f$1);
    }
}
