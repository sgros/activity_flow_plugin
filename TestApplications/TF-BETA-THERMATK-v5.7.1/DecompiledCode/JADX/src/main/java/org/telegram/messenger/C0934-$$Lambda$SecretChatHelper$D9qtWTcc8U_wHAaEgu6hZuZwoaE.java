package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_encryptedChatDiscarded;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SecretChatHelper$D9qtWTcc8U_wHAaEgu6hZuZwoaE */
public final /* synthetic */ class C0934-$$Lambda$SecretChatHelper$D9qtWTcc8U_wHAaEgu6hZuZwoaE implements Runnable {
    private final /* synthetic */ SecretChatHelper f$0;
    private final /* synthetic */ TL_encryptedChatDiscarded f$1;

    public /* synthetic */ C0934-$$Lambda$SecretChatHelper$D9qtWTcc8U_wHAaEgu6hZuZwoaE(SecretChatHelper secretChatHelper, TL_encryptedChatDiscarded tL_encryptedChatDiscarded) {
        this.f$0 = secretChatHelper;
        this.f$1 = tL_encryptedChatDiscarded;
    }

    public final void run() {
        this.f$0.lambda$processAcceptedSecretChat$18$SecretChatHelper(this.f$1);
    }
}
