package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.EncryptedChat;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SecretChatHelper$n0uu_vomtENRYh_Kxa0sgG1vkoo */
public final /* synthetic */ class C0952-$$Lambda$SecretChatHelper$n0uu_vomtENRYh_Kxa0sgG1vkoo implements Runnable {
    private final /* synthetic */ SecretChatHelper f$0;
    private final /* synthetic */ EncryptedChat f$1;

    public /* synthetic */ C0952-$$Lambda$SecretChatHelper$n0uu_vomtENRYh_Kxa0sgG1vkoo(SecretChatHelper secretChatHelper, EncryptedChat encryptedChat) {
        this.f$0 = secretChatHelper;
        this.f$1 = encryptedChat;
    }

    public final void run() {
        this.f$0.lambda$applyPeerLayer$8$SecretChatHelper(this.f$1);
    }
}
