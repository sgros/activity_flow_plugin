package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$YrH4Mr-BWV-6GLj5qWieGm840u8 */
public final /* synthetic */ class C3491-$$Lambda$MessagesController$YrH4Mr-BWV-6GLj5qWieGm840u8 implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3491-$$Lambda$MessagesController$YrH4Mr-BWV-6GLj5qWieGm840u8(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$reloadDialogsReadValue$12$MessagesController(tLObject, tL_error);
    }
}
