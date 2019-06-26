package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.EncryptedChat;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SecretChatHelper$G9V6FvkI-PnA0UumuoB_kxH2lOM */
public final /* synthetic */ class C0936-$$Lambda$SecretChatHelper$G9V6FvkI-PnA0UumuoB_kxH2lOM implements Runnable {
    private final /* synthetic */ SecretChatHelper f$0;
    private final /* synthetic */ EncryptedChat f$1;
    private final /* synthetic */ EncryptedChat f$2;

    public /* synthetic */ C0936-$$Lambda$SecretChatHelper$G9V6FvkI-PnA0UumuoB_kxH2lOM(SecretChatHelper secretChatHelper, EncryptedChat encryptedChat, EncryptedChat encryptedChat2) {
        this.f$0 = secretChatHelper;
        this.f$1 = encryptedChat;
        this.f$2 = encryptedChat2;
    }

    public final void run() {
        this.f$0.lambda$processUpdateEncryption$2$SecretChatHelper(this.f$1, this.f$2);
    }
}
