package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$cVyFjAxO3BTy-3AQHVKKSH7bs_0 */
public final /* synthetic */ class C3494-$$Lambda$MessagesController$cVyFjAxO3BTy-3AQHVKKSH7bs_0 implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3494-$$Lambda$MessagesController$cVyFjAxO3BTy-3AQHVKKSH7bs_0(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadCurrentState$190$MessagesController(tLObject, tL_error);
    }
}
