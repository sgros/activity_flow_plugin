package org.telegram.messenger;

import android.util.SparseArray;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$JW_1UL1JA9UbvXsdwarITz2ppQM */
public final /* synthetic */ class C3373-$$Lambda$DataQuery$JW_1UL1JA9UbvXsdwarITz2ppQM implements RequestDelegate {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ SparseArray f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C3373-$$Lambda$DataQuery$JW_1UL1JA9UbvXsdwarITz2ppQM(DataQuery dataQuery, SparseArray sparseArray, long j) {
        this.f$0 = dataQuery;
        this.f$1 = sparseArray;
        this.f$2 = j;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$92$DataQuery(this.f$1, this.f$2, tLObject, tL_error);
    }
}
