package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_messages_search;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$eJFDqCNJQlVs4Vxl5xT4VYdEsgw */
public final /* synthetic */ class C0394-$$Lambda$DataQuery$eJFDqCNJQlVs4Vxl5xT4VYdEsgw implements Runnable {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ TLObject f$2;
    private final /* synthetic */ TL_messages_search f$3;
    private final /* synthetic */ long f$4;
    private final /* synthetic */ int f$5;
    private final /* synthetic */ int f$6;
    private final /* synthetic */ User f$7;

    public /* synthetic */ C0394-$$Lambda$DataQuery$eJFDqCNJQlVs4Vxl5xT4VYdEsgw(DataQuery dataQuery, long j, TLObject tLObject, TL_messages_search tL_messages_search, long j2, int i, int i2, User user) {
        this.f$0 = dataQuery;
        this.f$1 = j;
        this.f$2 = tLObject;
        this.f$3 = tL_messages_search;
        this.f$4 = j2;
        this.f$5 = i;
        this.f$6 = i2;
        this.f$7 = user;
    }

    public final void run() {
        this.f$0.lambda$null$47$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
    }
}
