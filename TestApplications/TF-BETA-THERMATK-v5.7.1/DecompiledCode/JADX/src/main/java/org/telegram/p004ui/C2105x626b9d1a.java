package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC.TL_account_updatePasswordSettings;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$TwoStepVerificationActivity$6t0ApRpo2RwhN-WZ8Os0Xk1Kb5c */
public final /* synthetic */ class C2105x626b9d1a implements OnClickListener {
    private final /* synthetic */ TwoStepVerificationActivity f$0;
    private final /* synthetic */ byte[] f$1;
    private final /* synthetic */ TL_account_updatePasswordSettings f$2;

    public /* synthetic */ C2105x626b9d1a(TwoStepVerificationActivity twoStepVerificationActivity, byte[] bArr, TL_account_updatePasswordSettings tL_account_updatePasswordSettings) {
        this.f$0 = twoStepVerificationActivity;
        this.f$1 = bArr;
        this.f$2 = tL_account_updatePasswordSettings;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$21$TwoStepVerificationActivity(this.f$1, this.f$2, dialogInterface, i);
    }
}
