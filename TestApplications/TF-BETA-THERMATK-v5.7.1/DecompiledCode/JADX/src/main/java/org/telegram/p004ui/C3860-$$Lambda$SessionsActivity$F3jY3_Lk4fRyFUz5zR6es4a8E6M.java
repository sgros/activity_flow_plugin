package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_webAuthorization;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$SessionsActivity$F3jY3_Lk4fRyFUz5zR6es4a8E6M */
public final /* synthetic */ class C3860-$$Lambda$SessionsActivity$F3jY3_Lk4fRyFUz5zR6es4a8E6M implements RequestDelegate {
    private final /* synthetic */ SessionsActivity f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ TL_webAuthorization f$2;

    public /* synthetic */ C3860-$$Lambda$SessionsActivity$F3jY3_Lk4fRyFUz5zR6es4a8E6M(SessionsActivity sessionsActivity, AlertDialog alertDialog, TL_webAuthorization tL_webAuthorization) {
        this.f$0 = sessionsActivity;
        this.f$1 = alertDialog;
        this.f$2 = tL_webAuthorization;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$9$SessionsActivity(this.f$1, this.f$2, tLObject, tL_error);
    }
}
