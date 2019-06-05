package org.mozilla.focus.utils;

import android.content.Context;
import android.support.p004v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.utils.-$$Lambda$DialogUtils$wEBYFVSzBa1L5mrkhE3W7pluRxY */
public final /* synthetic */ class C0535-$$Lambda$DialogUtils$wEBYFVSzBa1L5mrkhE3W7pluRxY implements OnClickListener {
    private final /* synthetic */ Context f$0;
    private final /* synthetic */ AlertDialog f$1;

    public /* synthetic */ C0535-$$Lambda$DialogUtils$wEBYFVSzBa1L5mrkhE3W7pluRxY(Context context, AlertDialog alertDialog) {
        this.f$0 = context;
        this.f$1 = alertDialog;
    }

    public final void onClick(View view) {
        DialogUtils.lambda$showRateAppDialog$2(this.f$0, this.f$1, view);
    }
}
