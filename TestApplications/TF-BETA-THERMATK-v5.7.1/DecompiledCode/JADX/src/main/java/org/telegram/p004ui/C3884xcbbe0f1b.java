package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$TwoStepVerificationActivity$juf-ayzPNKWkx2ZD2C5VwnmYfxI */
public final /* synthetic */ class C3884xcbbe0f1b implements RequestDelegate {
    private final /* synthetic */ TwoStepVerificationActivity f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ C3884xcbbe0f1b(TwoStepVerificationActivity twoStepVerificationActivity, boolean z) {
        this.f$0 = twoStepVerificationActivity;
        this.f$1 = z;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$20$TwoStepVerificationActivity(this.f$1, tLObject, tL_error);
    }
}
