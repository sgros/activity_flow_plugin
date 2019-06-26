package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$cWol_41rOBrStFY3FKcjDkmBodE */
public final /* synthetic */ class C0679-$$Lambda$MessagesController$cWol_41rOBrStFY3FKcjDkmBodE implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ MessageObject f$1;

    public /* synthetic */ C0679-$$Lambda$MessagesController$cWol_41rOBrStFY3FKcjDkmBodE(MessagesController messagesController, MessageObject messageObject) {
        this.f$0 = messagesController;
        this.f$1 = messageObject;
    }

    public final void run() {
        this.f$0.lambda$addToViewsQueue$139$MessagesController(this.f$1);
    }
}
