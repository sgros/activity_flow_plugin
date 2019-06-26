package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.LoginActivity.LoginActivityRecoverView;
import org.telegram.tgnet.TLObject;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LoginActivity$LoginActivityRecoverView$I1bQ5rEP_TTiI07Nb2wIWAeHEMM */
public final /* synthetic */ class C1620x8e6a3818 implements OnClickListener {
    private final /* synthetic */ LoginActivityRecoverView f$0;
    private final /* synthetic */ TLObject f$1;

    public /* synthetic */ C1620x8e6a3818(LoginActivityRecoverView loginActivityRecoverView, TLObject tLObject) {
        this.f$0 = loginActivityRecoverView;
        this.f$1 = tLObject;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$3$LoginActivity$LoginActivityRecoverView(this.f$1, dialogInterface, i);
    }
}
