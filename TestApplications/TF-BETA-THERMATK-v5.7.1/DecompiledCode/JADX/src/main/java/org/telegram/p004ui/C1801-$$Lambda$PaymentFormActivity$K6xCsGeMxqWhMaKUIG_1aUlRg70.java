package org.telegram.p004ui;

import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_payments_sendPaymentForm;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PaymentFormActivity$K6xCsGeMxqWhMaKUIG_1aUlRg70 */
public final /* synthetic */ class C1801-$$Lambda$PaymentFormActivity$K6xCsGeMxqWhMaKUIG_1aUlRg70 implements Runnable {
    private final /* synthetic */ PaymentFormActivity f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ TL_payments_sendPaymentForm f$2;

    public /* synthetic */ C1801-$$Lambda$PaymentFormActivity$K6xCsGeMxqWhMaKUIG_1aUlRg70(PaymentFormActivity paymentFormActivity, TL_error tL_error, TL_payments_sendPaymentForm tL_payments_sendPaymentForm) {
        this.f$0 = paymentFormActivity;
        this.f$1 = tL_error;
        this.f$2 = tL_payments_sendPaymentForm;
    }

    public final void run() {
        this.f$0.lambda$null$39$PaymentFormActivity(this.f$1, this.f$2);
    }
}
