package org.telegram.messenger;

import org.telegram.messenger.MessagesStorage.IntCallback;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$4QgjEUgSmRBJDMucSQiTE5AXnrw */
public final /* synthetic */ class C0749-$$Lambda$MessagesStorage$4QgjEUgSmRBJDMucSQiTE5AXnrw implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ IntCallback f$2;

    public /* synthetic */ C0749-$$Lambda$MessagesStorage$4QgjEUgSmRBJDMucSQiTE5AXnrw(MessagesStorage messagesStorage, long j, IntCallback intCallback) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = intCallback;
    }

    public final void run() {
        this.f$0.lambda$getMessagesCount$98$MessagesStorage(this.f$1, this.f$2);
    }
}
