package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Updates;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$4Kecy4z15gyx9k2PnYvYSIzID4o */
public final /* synthetic */ class C0960-$$Lambda$SendMessagesHelper$4Kecy4z15gyx9k2PnYvYSIzID4o implements Runnable {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ Updates f$1;

    public /* synthetic */ C0960-$$Lambda$SendMessagesHelper$4Kecy4z15gyx9k2PnYvYSIzID4o(SendMessagesHelper sendMessagesHelper, Updates updates) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = updates;
    }

    public final void run() {
        this.f$0.lambda$null$27$SendMessagesHelper(this.f$1);
    }
}
