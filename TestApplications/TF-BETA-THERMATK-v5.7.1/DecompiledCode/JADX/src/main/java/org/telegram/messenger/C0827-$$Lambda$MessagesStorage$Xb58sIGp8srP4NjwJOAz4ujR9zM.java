package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.EncryptedChat;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$Xb58sIGp8srP4NjwJOAz4ujR9zM */
public final /* synthetic */ class C0827-$$Lambda$MessagesStorage$Xb58sIGp8srP4NjwJOAz4ujR9zM implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ EncryptedChat f$1;

    public /* synthetic */ C0827-$$Lambda$MessagesStorage$Xb58sIGp8srP4NjwJOAz4ujR9zM(MessagesStorage messagesStorage, EncryptedChat encryptedChat) {
        this.f$0 = messagesStorage;
        this.f$1 = encryptedChat;
    }

    public final void run() {
        this.f$0.lambda$updateEncryptedChat$107$MessagesStorage(this.f$1);
    }
}
