package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Message;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$9sCQAO18nnba6ToshnaeVPhZWRk */
public final /* synthetic */ class C0347-$$Lambda$DataQuery$9sCQAO18nnba6ToshnaeVPhZWRk implements Runnable {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ Message f$1;

    public /* synthetic */ C0347-$$Lambda$DataQuery$9sCQAO18nnba6ToshnaeVPhZWRk(DataQuery dataQuery, Message message) {
        this.f$0 = dataQuery;
        this.f$1 = message;
    }

    public final void run() {
        this.f$0.lambda$savePinnedMessage$88$DataQuery(this.f$1);
    }
}