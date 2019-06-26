package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$YebMv5xtYdxqizkIFhqWdfrEUcI */
public final /* synthetic */ class C3489-$$Lambda$MessagesController$YebMv5xtYdxqizkIFhqWdfrEUcI implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3489-$$Lambda$MessagesController$YebMv5xtYdxqizkIFhqWdfrEUcI(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$toogleChannelInvitesHistory$171$MessagesController(tLObject, tL_error);
    }
}
