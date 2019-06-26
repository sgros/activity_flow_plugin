package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$TwoStepVerificationActivity$f9cuV_saSqd5BKXFxtWSGi8SzTE */
public final /* synthetic */ class C3883xf9422202 implements RequestDelegate {
    private final /* synthetic */ TwoStepVerificationActivity f$0;

    public /* synthetic */ C3883xf9422202(TwoStepVerificationActivity twoStepVerificationActivity) {
        this.f$0 = twoStepVerificationActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$processDone$36$TwoStepVerificationActivity(tLObject, tL_error);
    }
}
