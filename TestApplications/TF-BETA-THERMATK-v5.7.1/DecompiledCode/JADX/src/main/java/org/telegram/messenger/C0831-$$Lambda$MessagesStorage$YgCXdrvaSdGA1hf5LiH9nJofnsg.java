package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$YgCXdrvaSdGA1hf5LiH9nJofnsg */
public final /* synthetic */ class C0831-$$Lambda$MessagesStorage$YgCXdrvaSdGA1hf5LiH9nJofnsg implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ User f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C0831-$$Lambda$MessagesStorage$YgCXdrvaSdGA1hf5LiH9nJofnsg(MessagesStorage messagesStorage, User user, boolean z, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = user;
        this.f$2 = z;
        this.f$3 = i;
    }

    public final void run() {
        this.f$0.lambda$loadUserInfo$73$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}
