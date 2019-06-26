package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$rOfsCqpWEwvJI16XzaGq8LurqHg */
public final /* synthetic */ class C3516-$$Lambda$MessagesController$rOfsCqpWEwvJI16XzaGq8LurqHg implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3516-$$Lambda$MessagesController$rOfsCqpWEwvJI16XzaGq8LurqHg(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadSignUpNotificationsSettings$117$MessagesController(tLObject, tL_error);
    }
}
