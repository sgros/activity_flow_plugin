package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$TwoStepVerificationActivity$9nDa_eoqxtx9fYaZZmibmVZpEjY */
public final /* synthetic */ class C3878x92db96b4 implements RequestDelegate {
    private final /* synthetic */ TwoStepVerificationActivity f$0;
    private final /* synthetic */ byte[] f$1;
    private final /* synthetic */ byte[] f$2;

    public /* synthetic */ C3878x92db96b4(TwoStepVerificationActivity twoStepVerificationActivity, byte[] bArr, byte[] bArr2) {
        this.f$0 = twoStepVerificationActivity;
        this.f$1 = bArr;
        this.f$2 = bArr2;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$32$TwoStepVerificationActivity(this.f$1, this.f$2, tLObject, tL_error);
    }
}
