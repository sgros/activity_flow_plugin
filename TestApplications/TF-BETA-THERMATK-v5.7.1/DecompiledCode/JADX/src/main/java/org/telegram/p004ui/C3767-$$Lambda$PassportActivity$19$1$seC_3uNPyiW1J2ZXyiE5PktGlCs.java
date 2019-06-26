package org.telegram.p004ui;

import org.telegram.p004ui.PassportActivity.C425019.C42491;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_secureValue;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$19$1$seC_3uNPyiW1J2ZXyiE5PktGlCs */
public final /* synthetic */ class C3767-$$Lambda$PassportActivity$19$1$seC_3uNPyiW1J2ZXyiE5PktGlCs implements RequestDelegate {
    private final /* synthetic */ C42491 f$0;
    private final /* synthetic */ TL_secureValue f$1;

    public /* synthetic */ C3767-$$Lambda$PassportActivity$19$1$seC_3uNPyiW1J2ZXyiE5PktGlCs(C42491 c42491, TL_secureValue tL_secureValue) {
        this.f$0 = c42491;
        this.f$1 = tL_secureValue;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$run$4$PassportActivity$19$1(this.f$1, tLObject, tL_error);
    }
}
