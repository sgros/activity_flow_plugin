package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatLinkActivity$Hzb-oINiZZCK4So4QPZpn91gK3A */
public final /* synthetic */ class C3647-$$Lambda$ChatLinkActivity$Hzb-oINiZZCK4So4QPZpn91gK3A implements RequestDelegate {
    private final /* synthetic */ ChatLinkActivity f$0;
    private final /* synthetic */ AlertDialog[] f$1;

    public /* synthetic */ C3647-$$Lambda$ChatLinkActivity$Hzb-oINiZZCK4So4QPZpn91gK3A(ChatLinkActivity chatLinkActivity, AlertDialog[] alertDialogArr) {
        this.f$0 = chatLinkActivity;
        this.f$1 = alertDialogArr;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$1$ChatLinkActivity(this.f$1, tLObject, tL_error);
    }
}
