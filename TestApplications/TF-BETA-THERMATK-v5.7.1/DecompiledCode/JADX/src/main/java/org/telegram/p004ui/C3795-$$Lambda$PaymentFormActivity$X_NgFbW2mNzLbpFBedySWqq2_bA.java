package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA */
public final /* synthetic */ class C3795-$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA implements RequestDelegate {
    private final /* synthetic */ PaymentFormActivity f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ C3795-$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA(PaymentFormActivity paymentFormActivity, boolean z, String str) {
        this.f$0 = paymentFormActivity;
        this.f$1 = z;
        this.f$2 = str;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$32$PaymentFormActivity(this.f$1, this.f$2, tLObject, tL_error);
    }
}
