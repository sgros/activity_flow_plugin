package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChangeUsernameActivity$iXI1HI9G_hbgkQiH6y4X4YZM9HI */
public final /* synthetic */ class C1263-$$Lambda$ChangeUsernameActivity$iXI1HI9G_hbgkQiH6y4X4YZM9HI implements Runnable {
    private final /* synthetic */ ChangeUsernameActivity f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ User f$2;

    public /* synthetic */ C1263-$$Lambda$ChangeUsernameActivity$iXI1HI9G_hbgkQiH6y4X4YZM9HI(ChangeUsernameActivity changeUsernameActivity, AlertDialog alertDialog, User user) {
        this.f$0 = changeUsernameActivity;
        this.f$1 = alertDialog;
        this.f$2 = user;
    }

    public final void run() {
        this.f$0.lambda$null$5$ChangeUsernameActivity(this.f$1, this.f$2);
    }
}
