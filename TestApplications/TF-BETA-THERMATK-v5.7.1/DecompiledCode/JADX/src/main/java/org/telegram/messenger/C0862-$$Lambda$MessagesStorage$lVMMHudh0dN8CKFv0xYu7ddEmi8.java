package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$lVMMHudh0dN8CKFv0xYu7ddEmi8 */
public final /* synthetic */ class C0862-$$Lambda$MessagesStorage$lVMMHudh0dN8CKFv0xYu7ddEmi8 implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ MessageObject f$1;

    public /* synthetic */ C0862-$$Lambda$MessagesStorage$lVMMHudh0dN8CKFv0xYu7ddEmi8(MessagesStorage messagesStorage, MessageObject messageObject) {
        this.f$0 = messagesStorage;
        this.f$1 = messageObject;
    }

    public final void run() {
        this.f$0.lambda$putPushMessage$25$MessagesStorage(this.f$1);
    }
}
