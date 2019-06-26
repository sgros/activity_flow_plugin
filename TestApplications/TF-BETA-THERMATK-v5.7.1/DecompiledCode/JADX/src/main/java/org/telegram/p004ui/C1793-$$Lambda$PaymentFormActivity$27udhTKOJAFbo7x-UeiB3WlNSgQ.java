package org.telegram.p004ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PaymentFormActivity$27udhTKOJAFbo7x-UeiB3WlNSgQ */
public final /* synthetic */ class C1793-$$Lambda$PaymentFormActivity$27udhTKOJAFbo7x-UeiB3WlNSgQ implements Runnable {
    private final /* synthetic */ PaymentFormActivity f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ TLObject f$2;

    public /* synthetic */ C1793-$$Lambda$PaymentFormActivity$27udhTKOJAFbo7x-UeiB3WlNSgQ(PaymentFormActivity paymentFormActivity, TL_error tL_error, TLObject tLObject) {
        this.f$0 = paymentFormActivity;
        this.f$1 = tL_error;
        this.f$2 = tLObject;
    }

    public final void run() {
        this.f$0.lambda$null$23$PaymentFormActivity(this.f$1, this.f$2);
    }
}
