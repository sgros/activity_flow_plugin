package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$PRISru-yyJ8MquHeYUjmNXD8lxM */
public final /* synthetic */ class C1577-$$Lambda$LaunchActivity$PRISru-yyJ8MquHeYUjmNXD8lxM implements Runnable {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ Bundle f$1;
    private final /* synthetic */ Integer f$2;
    private final /* synthetic */ int[] f$3;
    private final /* synthetic */ AlertDialog f$4;
    private final /* synthetic */ BaseFragment f$5;
    private final /* synthetic */ int f$6;

    public /* synthetic */ C1577-$$Lambda$LaunchActivity$PRISru-yyJ8MquHeYUjmNXD8lxM(LaunchActivity launchActivity, Bundle bundle, Integer num, int[] iArr, AlertDialog alertDialog, BaseFragment baseFragment, int i) {
        this.f$0 = launchActivity;
        this.f$1 = bundle;
        this.f$2 = num;
        this.f$3 = iArr;
        this.f$4 = alertDialog;
        this.f$5 = baseFragment;
        this.f$6 = i;
    }

    public final void run() {
        this.f$0.lambda$runLinkRequest$32$LaunchActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
