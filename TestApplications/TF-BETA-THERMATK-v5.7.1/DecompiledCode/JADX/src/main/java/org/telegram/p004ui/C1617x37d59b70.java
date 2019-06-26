package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.LoginActivity.LoginActivityPasswordView;
import org.telegram.tgnet.TLRPC.TL_auth_passwordRecovery;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivityPasswordView$xtZEvWAVK5tRPP93-2SWWR4hemw */
public final /* synthetic */ class C1617x37d59b70 implements OnClickListener {
    private final /* synthetic */ LoginActivityPasswordView f$0;
    private final /* synthetic */ TL_auth_passwordRecovery f$1;

    public /* synthetic */ C1617x37d59b70(LoginActivityPasswordView loginActivityPasswordView, TL_auth_passwordRecovery tL_auth_passwordRecovery) {
        this.f$0 = loginActivityPasswordView;
        this.f$1 = tL_auth_passwordRecovery;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$1$LoginActivity$LoginActivityPasswordView(this.f$1, dialogInterface, i);
    }
}
