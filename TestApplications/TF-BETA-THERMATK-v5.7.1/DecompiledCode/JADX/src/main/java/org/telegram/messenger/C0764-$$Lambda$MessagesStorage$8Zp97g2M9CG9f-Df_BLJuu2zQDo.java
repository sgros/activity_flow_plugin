package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.EncryptedChat;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$8Zp97g2M9CG9f-Df_BLJuu2zQDo */
public final /* synthetic */ class C0764-$$Lambda$MessagesStorage$8Zp97g2M9CG9f-Df_BLJuu2zQDo implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ EncryptedChat f$1;

    public /* synthetic */ C0764-$$Lambda$MessagesStorage$8Zp97g2M9CG9f-Df_BLJuu2zQDo(MessagesStorage messagesStorage, EncryptedChat encryptedChat) {
        this.f$0 = messagesStorage;
        this.f$1 = encryptedChat;
    }

    public final void run() {
        this.f$0.lambda$updateEncryptedChatLayer$106$MessagesStorage(this.f$1);
    }
}