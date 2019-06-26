package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$9B6lLHcMl9ABhpIDa9aeiv_K8oA */
public final /* synthetic */ class C3453-$$Lambda$MessagesController$9B6lLHcMl9ABhpIDa9aeiv_K8oA implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C3453-$$Lambda$MessagesController$9B6lLHcMl9ABhpIDa9aeiv_K8oA(MessagesController messagesController, int i, long j) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = j;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$deleteMessages$61$MessagesController(this.f$1, this.f$2, tLObject, tL_error);
    }
}
