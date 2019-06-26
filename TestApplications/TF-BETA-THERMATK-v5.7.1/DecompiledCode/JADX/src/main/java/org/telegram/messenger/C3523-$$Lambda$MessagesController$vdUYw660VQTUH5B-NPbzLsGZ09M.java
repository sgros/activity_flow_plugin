package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$vdUYw660VQTUH5B-NPbzLsGZ09M */
public final /* synthetic */ class C3523-$$Lambda$MessagesController$vdUYw660VQTUH5B-NPbzLsGZ09M implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ C3523-$$Lambda$MessagesController$vdUYw660VQTUH5B-NPbzLsGZ09M(MessagesController messagesController, long j) {
        this.f$0 = messagesController;
        this.f$1 = j;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$markDialogAsUnread$215$MessagesController(this.f$1, tLObject, tL_error);
    }
}
