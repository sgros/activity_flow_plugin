package org.telegram.messenger.browser;

import android.content.Context;
import android.net.Uri;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.browser.-$$Lambda$Browser$UGNHp8wusapZ_BRGXGaD_Ayz4-I */
public final /* synthetic */ class C3554-$$Lambda$Browser$UGNHp8wusapZ_BRGXGaD_Ayz4-I implements RequestDelegate {
    private final /* synthetic */ AlertDialog[] f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ Uri f$2;
    private final /* synthetic */ Context f$3;
    private final /* synthetic */ boolean f$4;

    public /* synthetic */ C3554-$$Lambda$Browser$UGNHp8wusapZ_BRGXGaD_Ayz4-I(AlertDialog[] alertDialogArr, int i, Uri uri, Context context, boolean z) {
        this.f$0 = alertDialogArr;
        this.f$1 = i;
        this.f$2 = uri;
        this.f$3 = context;
        this.f$4 = z;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1075-$$Lambda$Browser$37_x46nyg5sVKyQShvHeLalxsGQ(this.f$0, tLObject, this.f$1, this.f$2, this.f$3, this.f$4));
    }
}
