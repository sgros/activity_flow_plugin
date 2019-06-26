package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.EncryptedChat;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$va5klSdwmh7kuOLc57NT0kZlbDY */
public final /* synthetic */ class C0884-$$Lambda$MessagesStorage$va5klSdwmh7kuOLc57NT0kZlbDY implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ EncryptedChat f$1;

    public /* synthetic */ C0884-$$Lambda$MessagesStorage$va5klSdwmh7kuOLc57NT0kZlbDY(MessagesStorage messagesStorage, EncryptedChat encryptedChat) {
        this.f$0 = messagesStorage;
        this.f$1 = encryptedChat;
    }

    public final void run() {
        this.f$0.lambda$updateEncryptedChatTTL$105$MessagesStorage(this.f$1);
    }
}
