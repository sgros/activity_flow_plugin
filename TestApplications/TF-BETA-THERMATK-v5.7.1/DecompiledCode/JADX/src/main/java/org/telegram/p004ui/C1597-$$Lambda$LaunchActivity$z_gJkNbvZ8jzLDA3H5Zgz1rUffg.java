package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.TLObject;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$z_gJkNbvZ8jzLDA3H5Zgz1rUffg */
public final /* synthetic */ class C1597-$$Lambda$LaunchActivity$z_gJkNbvZ8jzLDA3H5Zgz1rUffg implements Runnable {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ TLObject f$2;
    private final /* synthetic */ BaseFragment f$3;
    private final /* synthetic */ int f$4;
    private final /* synthetic */ Bundle f$5;

    public /* synthetic */ C1597-$$Lambda$LaunchActivity$z_gJkNbvZ8jzLDA3H5Zgz1rUffg(LaunchActivity launchActivity, AlertDialog alertDialog, TLObject tLObject, BaseFragment baseFragment, int i, Bundle bundle) {
        this.f$0 = launchActivity;
        this.f$1 = alertDialog;
        this.f$2 = tLObject;
        this.f$3 = baseFragment;
        this.f$4 = i;
        this.f$5 = bundle;
    }

    public final void run() {
        this.f$0.lambda$null$30$LaunchActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}