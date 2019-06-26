package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$iSBguIqFt9uYneQ2bb0JRjCTxlQ */
public final /* synthetic */ class C3507-$$Lambda$MessagesController$iSBguIqFt9uYneQ2bb0JRjCTxlQ implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3507-$$Lambda$MessagesController$iSBguIqFt9uYneQ2bb0JRjCTxlQ(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$performLogout$186$MessagesController(tLObject, tL_error);
    }
}
