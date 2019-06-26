package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Updates;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$WQ_rgU9i_c841KG3_Zo7CgxwtnA */
public final /* synthetic */ class C0986-$$Lambda$SendMessagesHelper$WQ_rgU9i_c841KG3_Zo7CgxwtnA implements Runnable {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ Updates f$1;

    public /* synthetic */ C0986-$$Lambda$SendMessagesHelper$WQ_rgU9i_c841KG3_Zo7CgxwtnA(SendMessagesHelper sendMessagesHelper, Updates updates) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = updates;
    }

    public final void run() {
        this.f$0.lambda$null$36$SendMessagesHelper(this.f$1);
    }
}
