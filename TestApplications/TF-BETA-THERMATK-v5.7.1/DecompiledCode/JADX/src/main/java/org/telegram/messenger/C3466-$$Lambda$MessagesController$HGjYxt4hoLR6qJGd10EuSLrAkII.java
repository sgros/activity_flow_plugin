package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$HGjYxt4hoLR6qJGd10EuSLrAkII */
public final /* synthetic */ class C3466-$$Lambda$MessagesController$HGjYxt4hoLR6qJGd10EuSLrAkII implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3466-$$Lambda$MessagesController$HGjYxt4hoLR6qJGd10EuSLrAkII(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$markMentionMessageAsRead$143$MessagesController(tLObject, tL_error);
    }
}
