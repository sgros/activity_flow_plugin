package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$k9Ujiji1KdWARvma6ge0odbCoWk */
public final /* synthetic */ class C1586-$$Lambda$LaunchActivity$k9Ujiji1KdWARvma6ge0odbCoWk implements Runnable {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ TL_error f$2;
    private final /* synthetic */ TLObject f$3;
    private final /* synthetic */ int f$4;

    public /* synthetic */ C1586-$$Lambda$LaunchActivity$k9Ujiji1KdWARvma6ge0odbCoWk(LaunchActivity launchActivity, AlertDialog alertDialog, TL_error tL_error, TLObject tLObject, int i) {
        this.f$0 = launchActivity;
        this.f$1 = alertDialog;
        this.f$2 = tL_error;
        this.f$3 = tLObject;
        this.f$4 = i;
    }

    public final void run() {
        this.f$0.lambda$null$16$LaunchActivity(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
