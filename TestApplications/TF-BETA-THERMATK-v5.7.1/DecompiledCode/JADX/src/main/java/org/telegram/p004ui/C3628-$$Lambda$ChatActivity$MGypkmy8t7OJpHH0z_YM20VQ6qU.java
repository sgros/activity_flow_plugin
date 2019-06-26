package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$MGypkmy8t7OJpHH0z_YM20VQ6qU */
public final /* synthetic */ class C3628-$$Lambda$ChatActivity$MGypkmy8t7OJpHH0z_YM20VQ6qU implements RequestDelegate {
    private final /* synthetic */ ChatActivity f$0;

    public /* synthetic */ C3628-$$Lambda$ChatActivity$MGypkmy8t7OJpHH0z_YM20VQ6qU(ChatActivity chatActivity) {
        this.f$0 = chatActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$startEditingMessageObject$67$ChatActivity(tLObject, tL_error);
    }
}
