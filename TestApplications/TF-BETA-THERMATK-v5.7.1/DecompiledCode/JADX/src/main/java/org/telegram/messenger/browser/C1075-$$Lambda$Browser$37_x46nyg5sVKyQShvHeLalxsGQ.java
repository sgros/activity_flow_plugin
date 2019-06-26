package org.telegram.messenger.browser;

import android.content.Context;
import android.net.Uri;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.TLObject;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.browser.-$$Lambda$Browser$37_x46nyg5sVKyQShvHeLalxsGQ */
public final /* synthetic */ class C1075-$$Lambda$Browser$37_x46nyg5sVKyQShvHeLalxsGQ implements Runnable {
    private final /* synthetic */ AlertDialog[] f$0;
    private final /* synthetic */ TLObject f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ Uri f$3;
    private final /* synthetic */ Context f$4;
    private final /* synthetic */ boolean f$5;

    public /* synthetic */ C1075-$$Lambda$Browser$37_x46nyg5sVKyQShvHeLalxsGQ(AlertDialog[] alertDialogArr, TLObject tLObject, int i, Uri uri, Context context, boolean z) {
        this.f$0 = alertDialogArr;
        this.f$1 = tLObject;
        this.f$2 = i;
        this.f$3 = uri;
        this.f$4 = context;
        this.f$5 = z;
    }

    public final void run() {
        Browser.lambda$null$0(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
