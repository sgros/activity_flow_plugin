package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$3LcmzAiodHFy1RroGRficYduxV8 */
public final /* synthetic */ class C1565-$$Lambda$LaunchActivity$3LcmzAiodHFy1RroGRficYduxV8 implements Runnable {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ TLObject f$2;
    private final /* synthetic */ TL_error f$3;

    public /* synthetic */ C1565-$$Lambda$LaunchActivity$3LcmzAiodHFy1RroGRficYduxV8(LaunchActivity launchActivity, AlertDialog alertDialog, TLObject tLObject, TL_error tL_error) {
        this.f$0 = launchActivity;
        this.f$1 = alertDialog;
        this.f$2 = tLObject;
        this.f$3 = tL_error;
    }

    public final void run() {
        this.f$0.lambda$null$25$LaunchActivity(this.f$1, this.f$2, this.f$3);
    }
}
