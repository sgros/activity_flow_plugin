package org.mozilla.focus.utils;

import android.content.Context;
import android.support.p004v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.utils.-$$Lambda$DialogUtils$_X7YflwqscrGGtHVTw1w7wtGJB4 */
public final /* synthetic */ class C0532-$$Lambda$DialogUtils$_X7YflwqscrGGtHVTw1w7wtGJB4 implements OnClickListener {
    private final /* synthetic */ Context f$0;
    private final /* synthetic */ AlertDialog f$1;

    public /* synthetic */ C0532-$$Lambda$DialogUtils$_X7YflwqscrGGtHVTw1w7wtGJB4(Context context, AlertDialog alertDialog) {
        this.f$0 = context;
        this.f$1 = alertDialog;
    }

    public final void onClick(View view) {
        DialogUtils.lambda$showRateAppDialog$1(this.f$0, this.f$1, view);
    }
}
