package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$ueghtSvHFDbkkKRlzH3zhB7vPCY */
public final /* synthetic */ class C3522-$$Lambda$MessagesController$ueghtSvHFDbkkKRlzH3zhB7vPCY implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3522-$$Lambda$MessagesController$ueghtSvHFDbkkKRlzH3zhB7vPCY(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$updateTimerProc$78$MessagesController(tLObject, tL_error);
    }
}
