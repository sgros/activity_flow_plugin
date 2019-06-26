package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$t77TIdvR5oVS88nA5fB-WrU7owc */
public final /* synthetic */ class C3546-$$Lambda$SendMessagesHelper$t77TIdvR5oVS88nA5fB-WrU7owc implements RequestDelegate {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C3546-$$Lambda$SendMessagesHelper$t77TIdvR5oVS88nA5fB-WrU7owc(SendMessagesHelper sendMessagesHelper, String str) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = str;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$13$SendMessagesHelper(this.f$1, tLObject, tL_error);
    }
}
