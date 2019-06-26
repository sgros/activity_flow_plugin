package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$GZZLBgVSubugJqEDeBHWQk3eSTE */
public final /* synthetic */ class C3464-$$Lambda$MessagesController$GZZLBgVSubugJqEDeBHWQk3eSTE implements RequestDelegate {
    public static final /* synthetic */ C3464-$$Lambda$MessagesController$GZZLBgVSubugJqEDeBHWQk3eSTE INSTANCE = new C3464-$$Lambda$MessagesController$GZZLBgVSubugJqEDeBHWQk3eSTE();

    private /* synthetic */ C3464-$$Lambda$MessagesController$GZZLBgVSubugJqEDeBHWQk3eSTE() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        MessagesController.lambda$markMessageContentAsRead$140(tLObject, tL_error);
    }
}
