package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Chat;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$JQg5AlfE18YOamojW_v4DYkKyIA */
public final /* synthetic */ class C0630-$$Lambda$MessagesController$JQg5AlfE18YOamojW_v4DYkKyIA implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ Chat f$1;

    public /* synthetic */ C0630-$$Lambda$MessagesController$JQg5AlfE18YOamojW_v4DYkKyIA(MessagesController messagesController, Chat chat) {
        this.f$0 = messagesController;
        this.f$1 = chat;
    }

    public final void run() {
        this.f$0.lambda$putChat$11$MessagesController(this.f$1);
    }
}
