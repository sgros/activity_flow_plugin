package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$9PuTSEkyzwdd31UYsaKLfGdsGkM */
public final /* synthetic */ class C3454-$$Lambda$MessagesController$9PuTSEkyzwdd31UYsaKLfGdsGkM implements RequestDelegate {
    public static final /* synthetic */ C3454-$$Lambda$MessagesController$9PuTSEkyzwdd31UYsaKLfGdsGkM INSTANCE = new C3454-$$Lambda$MessagesController$9PuTSEkyzwdd31UYsaKLfGdsGkM();

    private /* synthetic */ C3454-$$Lambda$MessagesController$9PuTSEkyzwdd31UYsaKLfGdsGkM() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        MessagesController.lambda$processUpdates$236(tLObject, tL_error);
    }
}
