package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$gAx8uSIsyHdT1Tx6VIiWEu0UYbQ */
public final /* synthetic */ class C3502-$$Lambda$MessagesController$gAx8uSIsyHdT1Tx6VIiWEu0UYbQ implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3502-$$Lambda$MessagesController$gAx8uSIsyHdT1Tx6VIiWEu0UYbQ(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$markMessageContentAsRead$141$MessagesController(tLObject, tL_error);
    }
}
