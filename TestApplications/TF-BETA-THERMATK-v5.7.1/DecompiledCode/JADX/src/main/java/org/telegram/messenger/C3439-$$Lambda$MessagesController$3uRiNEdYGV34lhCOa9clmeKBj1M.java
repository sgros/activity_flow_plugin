package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$3uRiNEdYGV34lhCOa9clmeKBj1M */
public final /* synthetic */ class C3439-$$Lambda$MessagesController$3uRiNEdYGV34lhCOa9clmeKBj1M implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3439-$$Lambda$MessagesController$3uRiNEdYGV34lhCOa9clmeKBj1M(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$deleteUserPhoto$57$MessagesController(tLObject, tL_error);
    }
}
