package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$uhLM6ViQVMMdzN56xkU0rITU9CY */
public final /* synthetic */ class C3547-$$Lambda$SendMessagesHelper$uhLM6ViQVMMdzN56xkU0rITU9CY implements RequestDelegate {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ MessageObject f$1;
    private final /* synthetic */ String f$2;
    private final /* synthetic */ Runnable f$3;

    public /* synthetic */ C3547-$$Lambda$SendMessagesHelper$uhLM6ViQVMMdzN56xkU0rITU9CY(SendMessagesHelper sendMessagesHelper, MessageObject messageObject, String str, Runnable runnable) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = messageObject;
        this.f$2 = str;
        this.f$3 = runnable;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$sendVote$15$SendMessagesHelper(this.f$1, this.f$2, this.f$3, tLObject, tL_error);
    }
}
