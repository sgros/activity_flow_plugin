package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$S17IW-TdnLou1yG0GrX54gGAuDE */
public final /* synthetic */ class C3476-$$Lambda$MessagesController$S17IW-TdnLou1yG0GrX54gGAuDE implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C3476-$$Lambda$MessagesController$S17IW-TdnLou1yG0GrX54gGAuDE(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadHintDialogs$110$MessagesController(tLObject, tL_error);
    }
}
