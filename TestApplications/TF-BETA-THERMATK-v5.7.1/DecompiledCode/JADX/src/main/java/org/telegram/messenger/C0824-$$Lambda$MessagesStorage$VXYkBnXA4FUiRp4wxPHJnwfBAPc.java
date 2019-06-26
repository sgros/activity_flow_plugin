package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.UserFull;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$VXYkBnXA4FUiRp4wxPHJnwfBAPc */
public final /* synthetic */ class C0824-$$Lambda$MessagesStorage$VXYkBnXA4FUiRp4wxPHJnwfBAPc implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ UserFull f$2;

    public /* synthetic */ C0824-$$Lambda$MessagesStorage$VXYkBnXA4FUiRp4wxPHJnwfBAPc(MessagesStorage messagesStorage, boolean z, UserFull userFull) {
        this.f$0 = messagesStorage;
        this.f$1 = z;
        this.f$2 = userFull;
    }

    public final void run() {
        this.f$0.lambda$updateUserInfo$74$MessagesStorage(this.f$1, this.f$2);
    }
}
