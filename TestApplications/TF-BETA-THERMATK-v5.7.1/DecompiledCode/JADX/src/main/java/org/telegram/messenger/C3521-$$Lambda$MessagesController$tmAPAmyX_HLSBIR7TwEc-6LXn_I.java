package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$tmAPAmyX_HLSBIR7TwEc-6LXn_I */
public final /* synthetic */ class C3521-$$Lambda$MessagesController$tmAPAmyX_HLSBIR7TwEc-6LXn_I implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C3521-$$Lambda$MessagesController$tmAPAmyX_HLSBIR7TwEc-6LXn_I(MessagesController messagesController, int i) {
        this.f$0 = messagesController;
        this.f$1 = i;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$migrateDialogs$126$MessagesController(this.f$1, tLObject, tL_error);
    }
}