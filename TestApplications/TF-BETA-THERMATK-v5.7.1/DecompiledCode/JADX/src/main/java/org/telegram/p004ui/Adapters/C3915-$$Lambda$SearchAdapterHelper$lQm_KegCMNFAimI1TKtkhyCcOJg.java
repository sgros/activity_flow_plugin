package org.telegram.p004ui.Adapters;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$SearchAdapterHelper$lQm_KegCMNFAimI1TKtkhyCcOJg */
public final /* synthetic */ class C3915-$$Lambda$SearchAdapterHelper$lQm_KegCMNFAimI1TKtkhyCcOJg implements RequestDelegate {
    private final /* synthetic */ SearchAdapterHelper f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ String f$2;
    private final /* synthetic */ boolean f$3;

    public /* synthetic */ C3915-$$Lambda$SearchAdapterHelper$lQm_KegCMNFAimI1TKtkhyCcOJg(SearchAdapterHelper searchAdapterHelper, int i, String str, boolean z) {
        this.f$0 = searchAdapterHelper;
        this.f$1 = i;
        this.f$2 = str;
        this.f$3 = z;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$queryServerSearch$1$SearchAdapterHelper(this.f$1, this.f$2, this.f$3, tLObject, tL_error);
    }
}
