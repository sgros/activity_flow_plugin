package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$dxMathd1bP5-uSwd4Gt7ZvEOFtw */
public final /* synthetic */ class C3497-$$Lambda$MessagesController$dxMathd1bP5-uSwd4Gt7ZvEOFtw implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3497-$$Lambda$MessagesController$dxMathd1bP5-uSwd4Gt7ZvEOFtw(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$sendBotStart$176$MessagesController(tLObject, tL_error);
    }
}
