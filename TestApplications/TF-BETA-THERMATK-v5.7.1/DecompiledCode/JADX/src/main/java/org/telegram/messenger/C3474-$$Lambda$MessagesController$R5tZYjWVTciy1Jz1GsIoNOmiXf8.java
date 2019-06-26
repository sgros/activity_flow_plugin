package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$R5tZYjWVTciy1Jz1GsIoNOmiXf8 */
public final /* synthetic */ class C3474-$$Lambda$MessagesController$R5tZYjWVTciy1Jz1GsIoNOmiXf8 implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3474-$$Lambda$MessagesController$R5tZYjWVTciy1Jz1GsIoNOmiXf8(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$getBlockedUsers$54$MessagesController(tLObject, tL_error);
    }
}
