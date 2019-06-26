package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$opwePRyqFhALjQxPzCei2q6RZPU */
public final /* synthetic */ class C3514-$$Lambda$MessagesController$opwePRyqFhALjQxPzCei2q6RZPU implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ User f$1;

    public /* synthetic */ C3514-$$Lambda$MessagesController$opwePRyqFhALjQxPzCei2q6RZPU(MessagesController messagesController, User user) {
        this.f$0 = messagesController;
        this.f$1 = user;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$unblockUser$53$MessagesController(this.f$1, tLObject, tL_error);
    }
}
