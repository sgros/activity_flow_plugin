package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$5GO5q-JFS4BEFNcWBCNsIVVD9gY */
public final /* synthetic */ class C3444-$$Lambda$MessagesController$5GO5q-JFS4BEFNcWBCNsIVVD9gY implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3444-$$Lambda$MessagesController$5GO5q-JFS4BEFNcWBCNsIVVD9gY(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$checkTosUpdate$88$MessagesController(tLObject, tL_error);
    }
}
