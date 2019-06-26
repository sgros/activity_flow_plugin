package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$avFJzHcfb0E8c4wljuXOjwoiTOA */
public final /* synthetic */ class C3493-$$Lambda$MessagesController$avFJzHcfb0E8c4wljuXOjwoiTOA implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3493-$$Lambda$MessagesController$avFJzHcfb0E8c4wljuXOjwoiTOA(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$didReceivedNotification$4$MessagesController(tLObject, tL_error);
    }
}
