package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.TLRPC.TL_account_updateProfile;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChangeBioActivity$T2lBd_q-6K_Uw6G0hNAJdgSSX7I */
public final /* synthetic */ class C1236-$$Lambda$ChangeBioActivity$T2lBd_q-6K_Uw6G0hNAJdgSSX7I implements Runnable {
    private final /* synthetic */ ChangeBioActivity f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ TL_error f$2;
    private final /* synthetic */ TL_account_updateProfile f$3;

    public /* synthetic */ C1236-$$Lambda$ChangeBioActivity$T2lBd_q-6K_Uw6G0hNAJdgSSX7I(ChangeBioActivity changeBioActivity, AlertDialog alertDialog, TL_error tL_error, TL_account_updateProfile tL_account_updateProfile) {
        this.f$0 = changeBioActivity;
        this.f$1 = alertDialog;
        this.f$2 = tL_error;
        this.f$3 = tL_account_updateProfile;
    }

    public final void run() {
        this.f$0.lambda$null$3$ChangeBioActivity(this.f$1, this.f$2, this.f$3);
    }
}
