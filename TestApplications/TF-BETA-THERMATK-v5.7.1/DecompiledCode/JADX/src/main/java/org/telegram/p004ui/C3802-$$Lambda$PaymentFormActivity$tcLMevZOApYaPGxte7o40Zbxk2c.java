package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PaymentFormActivity$tcLMevZOApYaPGxte7o40Zbxk2c */
public final /* synthetic */ class C3802-$$Lambda$PaymentFormActivity$tcLMevZOApYaPGxte7o40Zbxk2c implements RequestDelegate {
    private final /* synthetic */ PaymentFormActivity f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ C3802-$$Lambda$PaymentFormActivity$tcLMevZOApYaPGxte7o40Zbxk2c(PaymentFormActivity paymentFormActivity, boolean z) {
        this.f$0 = paymentFormActivity;
        this.f$1 = z;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$29$PaymentFormActivity(this.f$1, tLObject, tL_error);
    }
}
