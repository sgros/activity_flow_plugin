package org.telegram.p004ui;

import org.telegram.p004ui.PassportActivity.C425019.C42491;
import org.telegram.p004ui.PassportActivity.ErrorRunnable;
import org.telegram.p004ui.PassportActivity.PassportActivityDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_secureRequiredType;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$19$1$20EqXlc4T1xja88z5UaATcwnhKs */
public final /* synthetic */ class C1704-$$Lambda$PassportActivity$19$1$20EqXlc4T1xja88z5UaATcwnhKs implements Runnable {
    private final /* synthetic */ C42491 f$0;
    private final /* synthetic */ TLObject f$1;
    private final /* synthetic */ String f$2;
    private final /* synthetic */ TL_secureRequiredType f$3;
    private final /* synthetic */ PassportActivityDelegate f$4;
    private final /* synthetic */ TL_error f$5;
    private final /* synthetic */ ErrorRunnable f$6;

    public /* synthetic */ C1704-$$Lambda$PassportActivity$19$1$20EqXlc4T1xja88z5UaATcwnhKs(C42491 c42491, TLObject tLObject, String str, TL_secureRequiredType tL_secureRequiredType, PassportActivityDelegate passportActivityDelegate, TL_error tL_error, ErrorRunnable errorRunnable) {
        this.f$0 = c42491;
        this.f$1 = tLObject;
        this.f$2 = str;
        this.f$3 = tL_secureRequiredType;
        this.f$4 = passportActivityDelegate;
        this.f$5 = tL_error;
        this.f$6 = errorRunnable;
    }

    public final void run() {
        this.f$0.lambda$null$1$PassportActivity$19$1(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
