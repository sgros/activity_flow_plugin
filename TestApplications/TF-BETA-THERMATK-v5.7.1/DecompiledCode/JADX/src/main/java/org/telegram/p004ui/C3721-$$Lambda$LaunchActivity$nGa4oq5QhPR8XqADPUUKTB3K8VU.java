package org.telegram.p004ui;

import android.os.Bundle;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$nGa4oq5QhPR8XqADPUUKTB3K8VU */
public final /* synthetic */ class C3721-$$Lambda$LaunchActivity$nGa4oq5QhPR8XqADPUUKTB3K8VU implements RequestDelegate {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ BaseFragment f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ Bundle f$4;

    public /* synthetic */ C3721-$$Lambda$LaunchActivity$nGa4oq5QhPR8XqADPUUKTB3K8VU(LaunchActivity launchActivity, AlertDialog alertDialog, BaseFragment baseFragment, int i, Bundle bundle) {
        this.f$0 = launchActivity;
        this.f$1 = alertDialog;
        this.f$2 = baseFragment;
        this.f$3 = i;
        this.f$4 = bundle;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$31$LaunchActivity(this.f$1, this.f$2, this.f$3, this.f$4, tLObject, tL_error);
    }
}
