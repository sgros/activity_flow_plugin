package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PrivacyControlActivity$wCT7rGu451A16C1KVTM9KhVzZrc */
public final /* synthetic */ class C1916-$$Lambda$PrivacyControlActivity$wCT7rGu451A16C1KVTM9KhVzZrc implements Runnable {
    private final /* synthetic */ PrivacyControlActivity f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ TL_error f$2;
    private final /* synthetic */ TLObject f$3;

    public /* synthetic */ C1916-$$Lambda$PrivacyControlActivity$wCT7rGu451A16C1KVTM9KhVzZrc(PrivacyControlActivity privacyControlActivity, AlertDialog alertDialog, TL_error tL_error, TLObject tLObject) {
        this.f$0 = privacyControlActivity;
        this.f$1 = alertDialog;
        this.f$2 = tL_error;
        this.f$3 = tLObject;
    }

    public final void run() {
        this.f$0.lambda$null$3$PrivacyControlActivity(this.f$1, this.f$2, this.f$3);
    }
}
