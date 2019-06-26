package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Updates;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$eAZO9_fJVDn41rVP_qwqU45S3MA */
public final /* synthetic */ class C0682-$$Lambda$MessagesController$eAZO9_fJVDn41rVP_qwqU45S3MA implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ Updates f$1;

    public /* synthetic */ C0682-$$Lambda$MessagesController$eAZO9_fJVDn41rVP_qwqU45S3MA(MessagesController messagesController, Updates updates) {
        this.f$0 = messagesController;
        this.f$1 = updates;
    }

    public final void run() {
        this.f$0.lambda$null$156$MessagesController(this.f$1);
    }
}
