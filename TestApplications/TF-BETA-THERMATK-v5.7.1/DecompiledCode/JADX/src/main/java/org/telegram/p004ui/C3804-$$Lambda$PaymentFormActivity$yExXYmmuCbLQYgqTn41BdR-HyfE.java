package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PaymentFormActivity$yExXYmmuCbLQYgqTn41BdR-HyfE */
public final /* synthetic */ class C3804-$$Lambda$PaymentFormActivity$yExXYmmuCbLQYgqTn41BdR-HyfE implements RequestDelegate {
    private final /* synthetic */ PaymentFormActivity f$0;

    public /* synthetic */ C3804-$$Lambda$PaymentFormActivity$yExXYmmuCbLQYgqTn41BdR-HyfE(PaymentFormActivity paymentFormActivity) {
        this.f$0 = paymentFormActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadPasswordInfo$24$PaymentFormActivity(tLObject, tL_error);
    }
}
