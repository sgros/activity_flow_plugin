package org.telegram.p004ui.ActionBar;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.ActionBar.-$$Lambda$BaseFragment$vXTvtAK8XZpLjv4Env96FSJndOM */
public final /* synthetic */ class C2174-$$Lambda$BaseFragment$vXTvtAK8XZpLjv4Env96FSJndOM implements OnDismissListener {
    private final /* synthetic */ BaseFragment f$0;
    private final /* synthetic */ OnDismissListener f$1;

    public /* synthetic */ C2174-$$Lambda$BaseFragment$vXTvtAK8XZpLjv4Env96FSJndOM(BaseFragment baseFragment, OnDismissListener onDismissListener) {
        this.f$0 = baseFragment;
        this.f$1 = onDismissListener;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$showDialog$0$BaseFragment(this.f$1, dialogInterface);
    }
}
