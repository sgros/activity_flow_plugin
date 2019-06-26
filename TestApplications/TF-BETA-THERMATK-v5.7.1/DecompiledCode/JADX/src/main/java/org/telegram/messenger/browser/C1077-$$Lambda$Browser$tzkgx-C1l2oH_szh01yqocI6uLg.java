package org.telegram.messenger.browser;

import org.telegram.p004ui.ActionBar.AlertDialog;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.browser.-$$Lambda$Browser$tzkgx-C1l2oH_szh01yqocI6uLg */
public final /* synthetic */ class C1077-$$Lambda$Browser$tzkgx-C1l2oH_szh01yqocI6uLg implements Runnable {
    private final /* synthetic */ AlertDialog[] f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1077-$$Lambda$Browser$tzkgx-C1l2oH_szh01yqocI6uLg(AlertDialog[] alertDialogArr, int i) {
        this.f$0 = alertDialogArr;
        this.f$1 = i;
    }

    public final void run() {
        Browser.lambda$openUrl$3(this.f$0, this.f$1);
    }
}
