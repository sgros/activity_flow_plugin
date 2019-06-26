package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.EncryptedChat;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SecretChatHelper$tDKre2aQQBiVO0S8VAHIlXCNFCM */
public final /* synthetic */ class C0954-$$Lambda$SecretChatHelper$tDKre2aQQBiVO0S8VAHIlXCNFCM implements Runnable {
    private final /* synthetic */ SecretChatHelper f$0;
    private final /* synthetic */ EncryptedChat f$1;

    public /* synthetic */ C0954-$$Lambda$SecretChatHelper$tDKre2aQQBiVO0S8VAHIlXCNFCM(SecretChatHelper secretChatHelper, EncryptedChat encryptedChat) {
        this.f$0 = secretChatHelper;
        this.f$1 = encryptedChat;
    }

    public final void run() {
        this.f$0.lambda$processAcceptedSecretChat$17$SecretChatHelper(this.f$1);
    }
}
