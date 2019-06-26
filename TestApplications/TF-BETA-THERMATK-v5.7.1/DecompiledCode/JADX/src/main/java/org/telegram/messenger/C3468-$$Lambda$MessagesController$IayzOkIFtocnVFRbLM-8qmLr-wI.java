package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$IayzOkIFtocnVFRbLM-8qmLr-wI */
public final /* synthetic */ class C3468-$$Lambda$MessagesController$IayzOkIFtocnVFRbLM-8qmLr-wI implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3468-$$Lambda$MessagesController$IayzOkIFtocnVFRbLM-8qmLr-wI(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$generateUpdateMessage$187$MessagesController(tLObject, tL_error);
    }
}
