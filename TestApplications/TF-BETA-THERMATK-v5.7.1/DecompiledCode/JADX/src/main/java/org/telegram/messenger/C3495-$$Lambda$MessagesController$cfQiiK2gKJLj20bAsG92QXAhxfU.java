package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$cfQiiK2gKJLj20bAsG92QXAhxfU */
public final /* synthetic */ class C3495-$$Lambda$MessagesController$cfQiiK2gKJLj20bAsG92QXAhxfU implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3495-$$Lambda$MessagesController$cfQiiK2gKJLj20bAsG92QXAhxfU(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$updateTimerProc$77$MessagesController(tLObject, tL_error);
    }
}
