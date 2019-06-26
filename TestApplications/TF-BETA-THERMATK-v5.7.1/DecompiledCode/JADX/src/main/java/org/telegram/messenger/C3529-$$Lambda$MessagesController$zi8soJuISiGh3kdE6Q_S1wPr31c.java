package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$zi8soJuISiGh3kdE6Q_S1wPr31c */
public final /* synthetic */ class C3529-$$Lambda$MessagesController$zi8soJuISiGh3kdE6Q_S1wPr31c implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3529-$$Lambda$MessagesController$zi8soJuISiGh3kdE6Q_S1wPr31c(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$pinMessage$63$MessagesController(tLObject, tL_error);
    }
}
