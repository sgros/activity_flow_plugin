package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$TwoStepVerificationActivity$zGkZlp2DnS104dUP2z3jhg8EgmM */
public final /* synthetic */ class C3887x1b134ac9 implements RequestDelegate {
    private final /* synthetic */ TwoStepVerificationActivity f$0;

    public /* synthetic */ C3887x1b134ac9(TwoStepVerificationActivity twoStepVerificationActivity) {
        this.f$0 = twoStepVerificationActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$sendEmailConfirm$39$TwoStepVerificationActivity(tLObject, tL_error);
    }
}
