package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC.TL_auth_passwordRecovery;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$TwoStepVerificationActivity$o3LczIT3DtDztCPkC-aGamfNhzE */
public final /* synthetic */ class C2122xf2e42804 implements OnClickListener {
    private final /* synthetic */ TwoStepVerificationActivity f$0;
    private final /* synthetic */ TL_auth_passwordRecovery f$1;

    public /* synthetic */ C2122xf2e42804(TwoStepVerificationActivity twoStepVerificationActivity, TL_auth_passwordRecovery tL_auth_passwordRecovery) {
        this.f$0 = twoStepVerificationActivity;
        this.f$1 = tL_auth_passwordRecovery;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$1$TwoStepVerificationActivity(this.f$1, dialogInterface, i);
    }
}
