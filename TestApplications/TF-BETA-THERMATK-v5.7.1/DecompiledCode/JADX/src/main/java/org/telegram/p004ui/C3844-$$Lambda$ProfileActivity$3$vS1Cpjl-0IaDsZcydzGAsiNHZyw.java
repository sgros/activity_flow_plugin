package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ProfileActivity.C43083;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ProfileActivity$3$vS1Cpjl-0IaDsZcydzGAsiNHZyw */
public final /* synthetic */ class C3844-$$Lambda$ProfileActivity$3$vS1Cpjl-0IaDsZcydzGAsiNHZyw implements RequestDelegate {
    private final /* synthetic */ C43083 f$0;
    private final /* synthetic */ AlertDialog[] f$1;

    public /* synthetic */ C3844-$$Lambda$ProfileActivity$3$vS1Cpjl-0IaDsZcydzGAsiNHZyw(C43083 c43083, AlertDialog[] alertDialogArr) {
        this.f$0 = c43083;
        this.f$1 = alertDialogArr;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$onItemClick$4$ProfileActivity$3(this.f$1, tLObject, tL_error);
    }
}
