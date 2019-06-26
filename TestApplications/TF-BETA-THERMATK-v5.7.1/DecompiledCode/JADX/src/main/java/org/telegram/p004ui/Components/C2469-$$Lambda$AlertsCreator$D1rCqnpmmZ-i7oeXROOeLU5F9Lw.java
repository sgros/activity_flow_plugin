package org.telegram.p004ui.Components;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$D1rCqnpmmZ-i7oeXROOeLU5F9Lw */
public final /* synthetic */ class C2469-$$Lambda$AlertsCreator$D1rCqnpmmZ-i7oeXROOeLU5F9Lw implements Runnable {
    private final /* synthetic */ AlertDialog[] f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ BaseFragment f$3;

    public /* synthetic */ C2469-$$Lambda$AlertsCreator$D1rCqnpmmZ-i7oeXROOeLU5F9Lw(AlertDialog[] alertDialogArr, int i, int i2, BaseFragment baseFragment) {
        this.f$0 = alertDialogArr;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = baseFragment;
    }

    public final void run() {
        AlertsCreator.lambda$createDeleteMessagesAlert$44(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
