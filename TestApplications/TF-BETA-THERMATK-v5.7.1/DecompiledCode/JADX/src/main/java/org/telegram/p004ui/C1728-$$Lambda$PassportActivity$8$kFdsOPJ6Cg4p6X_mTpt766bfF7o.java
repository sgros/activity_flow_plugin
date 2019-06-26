package org.telegram.p004ui;

import org.telegram.p004ui.PassportActivity.C42578;
import org.telegram.tgnet.TLRPC.TL_account_passwordSettings;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$8$kFdsOPJ6Cg4p6X_mTpt766bfF7o */
public final /* synthetic */ class C1728-$$Lambda$PassportActivity$8$kFdsOPJ6Cg4p6X_mTpt766bfF7o implements Runnable {
    private final /* synthetic */ C42578 f$0;
    private final /* synthetic */ TL_account_passwordSettings f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ byte[] f$3;

    public /* synthetic */ C1728-$$Lambda$PassportActivity$8$kFdsOPJ6Cg4p6X_mTpt766bfF7o(C42578 c42578, TL_account_passwordSettings tL_account_passwordSettings, boolean z, byte[] bArr) {
        this.f$0 = c42578;
        this.f$1 = tL_account_passwordSettings;
        this.f$2 = z;
        this.f$3 = bArr;
    }

    public final void run() {
        this.f$0.lambda$null$14$PassportActivity$8(this.f$1, this.f$2, this.f$3);
    }
}
