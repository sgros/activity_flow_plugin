package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$sZkwI2OJzSWG6qrblxHbvHglZsU */
public final /* synthetic */ class C3389-$$Lambda$DataQuery$sZkwI2OJzSWG6qrblxHbvHglZsU implements RequestDelegate {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ int f$4;
    private final /* synthetic */ int f$5;
    private final /* synthetic */ boolean f$6;

    public /* synthetic */ C3389-$$Lambda$DataQuery$sZkwI2OJzSWG6qrblxHbvHglZsU(DataQuery dataQuery, long j, int i, int i2, int i3, int i4, boolean z) {
        this.f$0 = dataQuery;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = i3;
        this.f$5 = i4;
        this.f$6 = z;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadMedia$51$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, tLObject, tL_error);
    }
}