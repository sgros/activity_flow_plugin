package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$aeXtWX5NT1IBK6iBhloNZ1bs-M4 */
public final /* synthetic */ class C3492-$$Lambda$MessagesController$aeXtWX5NT1IBK6iBhloNZ1bs-M4 implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3492-$$Lambda$MessagesController$aeXtWX5NT1IBK6iBhloNZ1bs-M4(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$toogleChannelSignatures$169$MessagesController(tLObject, tL_error);
    }
}
