package org.telegram.p004ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$TwoStepVerificationActivity$6TAE9CBLd9XTecWOIuc8_sMGfrI */
public final /* synthetic */ class C2104xb7a467bf implements Runnable {
    private final /* synthetic */ TwoStepVerificationActivity f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ TLObject f$2;

    public /* synthetic */ C2104xb7a467bf(TwoStepVerificationActivity twoStepVerificationActivity, TL_error tL_error, TLObject tLObject) {
        this.f$0 = twoStepVerificationActivity;
        this.f$1 = tL_error;
        this.f$2 = tLObject;
    }

    public final void run() {
        this.f$0.lambda$null$29$TwoStepVerificationActivity(this.f$1, this.f$2);
    }
}
