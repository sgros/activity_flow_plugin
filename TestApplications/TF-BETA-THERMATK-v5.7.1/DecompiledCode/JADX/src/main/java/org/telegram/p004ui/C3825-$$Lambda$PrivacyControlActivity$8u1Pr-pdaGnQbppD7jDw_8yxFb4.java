package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PrivacyControlActivity$8u1Pr-pdaGnQbppD7jDw_8yxFb4 */
public final /* synthetic */ class C3825-$$Lambda$PrivacyControlActivity$8u1Pr-pdaGnQbppD7jDw_8yxFb4 implements RequestDelegate {
    private final /* synthetic */ PrivacyControlActivity f$0;
    private final /* synthetic */ AlertDialog f$1;

    public /* synthetic */ C3825-$$Lambda$PrivacyControlActivity$8u1Pr-pdaGnQbppD7jDw_8yxFb4(PrivacyControlActivity privacyControlActivity, AlertDialog alertDialog) {
        this.f$0 = privacyControlActivity;
        this.f$1 = alertDialog;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$applyCurrentPrivacySettings$4$PrivacyControlActivity(this.f$1, tLObject, tL_error);
    }
}
