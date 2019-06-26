package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$f6Fg5cePsPVR4IQG1UUiIG6Ywco */
public final /* synthetic */ class C3501-$$Lambda$MessagesController$f6Fg5cePsPVR4IQG1UUiIG6Ywco implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3501-$$Lambda$MessagesController$f6Fg5cePsPVR4IQG1UUiIG6Ywco(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$completeReadTask$146$MessagesController(tLObject, tL_error);
    }
}
