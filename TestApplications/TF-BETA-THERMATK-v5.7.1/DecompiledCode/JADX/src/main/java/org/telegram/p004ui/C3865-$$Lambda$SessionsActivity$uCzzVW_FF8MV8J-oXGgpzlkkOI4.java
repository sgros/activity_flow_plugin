package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_authorization;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$SessionsActivity$uCzzVW_FF8MV8J-oXGgpzlkkOI4 */
public final /* synthetic */ class C3865-$$Lambda$SessionsActivity$uCzzVW_FF8MV8J-oXGgpzlkkOI4 implements RequestDelegate {
    private final /* synthetic */ SessionsActivity f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ TL_authorization f$2;

    public /* synthetic */ C3865-$$Lambda$SessionsActivity$uCzzVW_FF8MV8J-oXGgpzlkkOI4(SessionsActivity sessionsActivity, AlertDialog alertDialog, TL_authorization tL_authorization) {
        this.f$0 = sessionsActivity;
        this.f$1 = alertDialog;
        this.f$2 = tL_authorization;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$7$SessionsActivity(this.f$1, this.f$2, tLObject, tL_error);
    }
}
