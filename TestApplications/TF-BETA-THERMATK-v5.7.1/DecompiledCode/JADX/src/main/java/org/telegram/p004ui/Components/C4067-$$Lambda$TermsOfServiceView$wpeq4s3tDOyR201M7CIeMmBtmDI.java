package org.telegram.p004ui.Components;

import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$TermsOfServiceView$wpeq4s3tDOyR201M7CIeMmBtmDI */
public final /* synthetic */ class C4067-$$Lambda$TermsOfServiceView$wpeq4s3tDOyR201M7CIeMmBtmDI implements RequestDelegate {
    private final /* synthetic */ TermsOfServiceView f$0;
    private final /* synthetic */ AlertDialog f$1;

    public /* synthetic */ C4067-$$Lambda$TermsOfServiceView$wpeq4s3tDOyR201M7CIeMmBtmDI(TermsOfServiceView termsOfServiceView, AlertDialog alertDialog) {
        this.f$0 = termsOfServiceView;
        this.f$1 = alertDialog;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$1$TermsOfServiceView(this.f$1, tLObject, tL_error);
    }
}
