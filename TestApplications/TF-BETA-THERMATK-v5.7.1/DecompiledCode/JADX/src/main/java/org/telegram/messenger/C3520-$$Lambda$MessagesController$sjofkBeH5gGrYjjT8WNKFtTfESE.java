package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$sjofkBeH5gGrYjjT8WNKFtTfESE */
public final /* synthetic */ class C3520-$$Lambda$MessagesController$sjofkBeH5gGrYjjT8WNKFtTfESE implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C3520-$$Lambda$MessagesController$sjofkBeH5gGrYjjT8WNKFtTfESE(MessagesController messagesController, int i) {
        this.f$0 = messagesController;
        this.f$1 = i;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$updateTimerProc$85$MessagesController(this.f$1, tLObject, tL_error);
    }
}
