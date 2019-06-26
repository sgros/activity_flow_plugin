package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SecretChatHelper$mfQ9CBiHkuJ2sHYijldk5vxoP4M */
public final /* synthetic */ class C3538-$$Lambda$SecretChatHelper$mfQ9CBiHkuJ2sHYijldk5vxoP4M implements RequestDelegate {
    public static final /* synthetic */ C3538-$$Lambda$SecretChatHelper$mfQ9CBiHkuJ2sHYijldk5vxoP4M INSTANCE = new C3538-$$Lambda$SecretChatHelper$mfQ9CBiHkuJ2sHYijldk5vxoP4M();

    private /* synthetic */ C3538-$$Lambda$SecretChatHelper$mfQ9CBiHkuJ2sHYijldk5vxoP4M() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        SecretChatHelper.lambda$declineSecretChat$19(tLObject, tL_error);
    }
}
