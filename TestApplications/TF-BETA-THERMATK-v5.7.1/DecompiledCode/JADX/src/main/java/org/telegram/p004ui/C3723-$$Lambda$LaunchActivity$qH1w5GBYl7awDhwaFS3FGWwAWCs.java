package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$qH1w5GBYl7awDhwaFS3FGWwAWCs */
public final /* synthetic */ class C3723-$$Lambda$LaunchActivity$qH1w5GBYl7awDhwaFS3FGWwAWCs implements RequestDelegate {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ AlertDialog f$1;

    public /* synthetic */ C3723-$$Lambda$LaunchActivity$qH1w5GBYl7awDhwaFS3FGWwAWCs(LaunchActivity launchActivity, AlertDialog alertDialog) {
        this.f$0 = launchActivity;
        this.f$1 = alertDialog;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$runLinkRequest$24$LaunchActivity(this.f$1, tLObject, tL_error);
    }
}
