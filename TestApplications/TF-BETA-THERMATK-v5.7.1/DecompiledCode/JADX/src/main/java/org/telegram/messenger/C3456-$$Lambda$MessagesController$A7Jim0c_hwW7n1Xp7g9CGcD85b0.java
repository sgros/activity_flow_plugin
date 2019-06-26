package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$A7Jim0c_hwW7n1Xp7g9CGcD85b0 */
public final /* synthetic */ class C3456-$$Lambda$MessagesController$A7Jim0c_hwW7n1Xp7g9CGcD85b0 implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3456-$$Lambda$MessagesController$A7Jim0c_hwW7n1Xp7g9CGcD85b0(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadUnreadDialogs$217$MessagesController(tLObject, tL_error);
    }
}
