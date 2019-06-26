package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Message;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$pG7ILjNgxGBHl9Rr7Ps_HNlhdG8 */
public final /* synthetic */ class C0412-$$Lambda$DataQuery$pG7ILjNgxGBHl9Rr7Ps_HNlhdG8 implements Runnable {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ Message f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C0412-$$Lambda$DataQuery$pG7ILjNgxGBHl9Rr7Ps_HNlhdG8(DataQuery dataQuery, Message message, long j) {
        this.f$0 = dataQuery;
        this.f$1 = message;
        this.f$2 = j;
    }

    public final void run() {
        this.f$0.lambda$null$105$DataQuery(this.f$1, this.f$2);
    }
}
