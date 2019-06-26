package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$40Sp7FeVOZVQXMLSFubFHF48OI8 */
public final /* synthetic */ class C3440-$$Lambda$MessagesController$40Sp7FeVOZVQXMLSFubFHF48OI8 implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3440-$$Lambda$MessagesController$40Sp7FeVOZVQXMLSFubFHF48OI8(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$changeChatTitle$183$MessagesController(tLObject, tL_error);
    }
}
