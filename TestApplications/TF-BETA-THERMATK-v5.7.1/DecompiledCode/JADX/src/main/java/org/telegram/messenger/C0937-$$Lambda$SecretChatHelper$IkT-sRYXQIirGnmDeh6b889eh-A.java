package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Dialog;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SecretChatHelper$IkT-sRYXQIirGnmDeh6b889eh-A */
public final /* synthetic */ class C0937-$$Lambda$SecretChatHelper$IkT-sRYXQIirGnmDeh6b889eh-A implements Runnable {
    private final /* synthetic */ SecretChatHelper f$0;
    private final /* synthetic */ Dialog f$1;

    public /* synthetic */ C0937-$$Lambda$SecretChatHelper$IkT-sRYXQIirGnmDeh6b889eh-A(SecretChatHelper secretChatHelper, Dialog dialog) {
        this.f$0 = secretChatHelper;
        this.f$1 = dialog;
    }

    public final void run() {
        this.f$0.lambda$processUpdateEncryption$1$SecretChatHelper(this.f$1);
    }
}