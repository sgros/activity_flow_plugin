package org.telegram.p004ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$TwoStepVerificationActivity$4P2nkSO_oWh_p7A_7V0YWykRkO0 */
public final /* synthetic */ class C2103x69718e15 implements Runnable {
    private final /* synthetic */ TwoStepVerificationActivity f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ TLObject f$2;
    private final /* synthetic */ boolean f$3;

    public /* synthetic */ C2103x69718e15(TwoStepVerificationActivity twoStepVerificationActivity, TL_error tL_error, TLObject tLObject, boolean z) {
        this.f$0 = twoStepVerificationActivity;
        this.f$1 = tL_error;
        this.f$2 = tLObject;
        this.f$3 = z;
    }

    public final void run() {
        this.f$0.lambda$null$13$TwoStepVerificationActivity(this.f$1, this.f$2, this.f$3);
    }
}
