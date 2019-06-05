package org.mozilla.focus.utils;

import android.content.Context;
import android.support.p004v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.utils.-$$Lambda$DialogUtils$28dtCamIzRB4NQvWqNCsRNPQFa4 */
public final /* synthetic */ class C0526-$$Lambda$DialogUtils$28dtCamIzRB4NQvWqNCsRNPQFa4 implements OnClickListener {
    private final /* synthetic */ AlertDialog f$0;
    private final /* synthetic */ Context f$1;

    public /* synthetic */ C0526-$$Lambda$DialogUtils$28dtCamIzRB4NQvWqNCsRNPQFa4(AlertDialog alertDialog, Context context) {
        this.f$0 = alertDialog;
        this.f$1 = context;
    }

    public final void onClick(View view) {
        DialogUtils.lambda$showShareAppDialog$5(this.f$0, this.f$1, view);
    }
}
