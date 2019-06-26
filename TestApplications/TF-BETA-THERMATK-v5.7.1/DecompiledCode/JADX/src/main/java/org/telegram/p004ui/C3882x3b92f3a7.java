package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$TwoStepVerificationActivity$XEU2eF294cmakN0jjZyU1ShYsjA */
public final /* synthetic */ class C3882x3b92f3a7 implements RequestDelegate {
    private final /* synthetic */ TwoStepVerificationActivity f$0;

    public /* synthetic */ C3882x3b92f3a7(TwoStepVerificationActivity twoStepVerificationActivity) {
        this.f$0 = twoStepVerificationActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$setNewPassword$18$TwoStepVerificationActivity(tLObject, tL_error);
    }
}
