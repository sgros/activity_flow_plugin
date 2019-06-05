package org.mozilla.focus.utils;

import android.content.Context;
import android.support.p004v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.utils.-$$Lambda$DialogUtils$T1sxZWz76nkX0mgkiZoiYiKYJP4 */
public final /* synthetic */ class C0531-$$Lambda$DialogUtils$T1sxZWz76nkX0mgkiZoiYiKYJP4 implements OnClickListener {
    private final /* synthetic */ Context f$0;
    private final /* synthetic */ AlertDialog f$1;

    public /* synthetic */ C0531-$$Lambda$DialogUtils$T1sxZWz76nkX0mgkiZoiYiKYJP4(Context context, AlertDialog alertDialog) {
        this.f$0 = context;
        this.f$1 = alertDialog;
    }

    public final void onClick(View view) {
        DialogUtils.lambda$showShareAppDialog$6(this.f$0, this.f$1, view);
    }
}
