package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.TLRPC.TL_authorization;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$SessionsActivity$We5_mg_HTuMQbN9C9uG4Vipc1GQ */
public final /* synthetic */ class C1975-$$Lambda$SessionsActivity$We5_mg_HTuMQbN9C9uG4Vipc1GQ implements Runnable {
    private final /* synthetic */ SessionsActivity f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ TL_error f$2;
    private final /* synthetic */ TL_authorization f$3;

    public /* synthetic */ C1975-$$Lambda$SessionsActivity$We5_mg_HTuMQbN9C9uG4Vipc1GQ(SessionsActivity sessionsActivity, AlertDialog alertDialog, TL_error tL_error, TL_authorization tL_authorization) {
        this.f$0 = sessionsActivity;
        this.f$1 = alertDialog;
        this.f$2 = tL_error;
        this.f$3 = tL_authorization;
    }

    public final void run() {
        this.f$0.lambda$null$6$SessionsActivity(this.f$1, this.f$2, this.f$3);
    }
}