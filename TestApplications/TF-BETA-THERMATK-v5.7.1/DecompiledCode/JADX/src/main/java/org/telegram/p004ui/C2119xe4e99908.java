package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC.TL_account_updatePasswordSettings;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$TwoStepVerificationActivity$dZl6L7A3pdg7u0zh2dz9t60mIJU */
public final /* synthetic */ class C2119xe4e99908 implements OnClickListener {
    private final /* synthetic */ TwoStepVerificationActivity f$0;
    private final /* synthetic */ byte[] f$1;
    private final /* synthetic */ TL_account_updatePasswordSettings f$2;

    public /* synthetic */ C2119xe4e99908(TwoStepVerificationActivity twoStepVerificationActivity, byte[] bArr, TL_account_updatePasswordSettings tL_account_updatePasswordSettings) {
        this.f$0 = twoStepVerificationActivity;
        this.f$1 = bArr;
        this.f$2 = tL_account_updatePasswordSettings;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$22$TwoStepVerificationActivity(this.f$1, this.f$2, dialogInterface, i);
    }
}
