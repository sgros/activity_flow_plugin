package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.EncryptedChat;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$9zeOYi8rbx6eRgRL0h0GGgHo_pM */
public final /* synthetic */ class C0769-$$Lambda$MessagesStorage$9zeOYi8rbx6eRgRL0h0GGgHo_pM implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ EncryptedChat f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ C0769-$$Lambda$MessagesStorage$9zeOYi8rbx6eRgRL0h0GGgHo_pM(MessagesStorage messagesStorage, EncryptedChat encryptedChat, boolean z) {
        this.f$0 = messagesStorage;
        this.f$1 = encryptedChat;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$updateEncryptedChatSeq$104$MessagesStorage(this.f$1, this.f$2);
    }
}
