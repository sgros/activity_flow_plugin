package org.telegram.p004ui;

import org.telegram.tgnet.TLRPC.TL_account_password;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PaymentFormActivity$k9SEAszMH3-nDlyEt9fLhgGlMAA */
public final /* synthetic */ class C1816-$$Lambda$PaymentFormActivity$k9SEAszMH3-nDlyEt9fLhgGlMAA implements Runnable {
    private final /* synthetic */ PaymentFormActivity f$0;
    private final /* synthetic */ TL_account_password f$1;
    private final /* synthetic */ byte[] f$2;

    public /* synthetic */ C1816-$$Lambda$PaymentFormActivity$k9SEAszMH3-nDlyEt9fLhgGlMAA(PaymentFormActivity paymentFormActivity, TL_account_password tL_account_password, byte[] bArr) {
        this.f$0 = paymentFormActivity;
        this.f$1 = tL_account_password;
        this.f$2 = bArr;
    }

    public final void run() {
        this.f$0.lambda$null$43$PaymentFormActivity(this.f$1, this.f$2);
    }
}