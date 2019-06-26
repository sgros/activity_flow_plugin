package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_wallPaper;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$KNg5IaCFYbOpTWjqZaxL1vwTVgk */
public final /* synthetic */ class C3712-$$Lambda$LaunchActivity$KNg5IaCFYbOpTWjqZaxL1vwTVgk implements RequestDelegate {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ AlertDialog f$1;
    private final /* synthetic */ TL_wallPaper f$2;

    public /* synthetic */ C3712-$$Lambda$LaunchActivity$KNg5IaCFYbOpTWjqZaxL1vwTVgk(LaunchActivity launchActivity, AlertDialog alertDialog, TL_wallPaper tL_wallPaper) {
        this.f$0 = launchActivity;
        this.f$1 = alertDialog;
        this.f$2 = tL_wallPaper;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$runLinkRequest$29$LaunchActivity(this.f$1, this.f$2, tLObject, tL_error);
    }
}
