package org.telegram.p004ui;

import org.telegram.p004ui.PassportActivity.C425019.C42491;
import org.telegram.p004ui.PassportActivity.ErrorRunnable;
import org.telegram.p004ui.PassportActivity.PassportActivityDelegate;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_secureRequiredType;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$19$1$z2A8tnBlw-sIzEUvPsIbORU04FQ */
public final /* synthetic */ class C3768-$$Lambda$PassportActivity$19$1$z2A8tnBlw-sIzEUvPsIbORU04FQ implements RequestDelegate {
    private final /* synthetic */ C42491 f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ TL_secureRequiredType f$2;
    private final /* synthetic */ PassportActivityDelegate f$3;
    private final /* synthetic */ ErrorRunnable f$4;

    public /* synthetic */ C3768-$$Lambda$PassportActivity$19$1$z2A8tnBlw-sIzEUvPsIbORU04FQ(C42491 c42491, String str, TL_secureRequiredType tL_secureRequiredType, PassportActivityDelegate passportActivityDelegate, ErrorRunnable errorRunnable) {
        this.f$0 = c42491;
        this.f$1 = str;
        this.f$2 = tL_secureRequiredType;
        this.f$3 = passportActivityDelegate;
        this.f$4 = errorRunnable;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$run$2$PassportActivity$19$1(this.f$1, this.f$2, this.f$3, this.f$4, tLObject, tL_error);
    }
}
