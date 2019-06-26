package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.EncryptedChat;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SecretChatHelper$UeLgl-NG4gDOs_Y4sJhukTdyyjM */
public final /* synthetic */ class C0943-$$Lambda$SecretChatHelper$UeLgl-NG4gDOs_Y4sJhukTdyyjM implements Runnable {
    private final /* synthetic */ SecretChatHelper f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ EncryptedChat f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C0943-$$Lambda$SecretChatHelper$UeLgl-NG4gDOs_Y4sJhukTdyyjM(SecretChatHelper secretChatHelper, int i, EncryptedChat encryptedChat, int i2) {
        this.f$0 = secretChatHelper;
        this.f$1 = i;
        this.f$2 = encryptedChat;
        this.f$3 = i2;
    }

    public final void run() {
        this.f$0.lambda$resendMessages$14$SecretChatHelper(this.f$1, this.f$2, this.f$3);
    }
}
