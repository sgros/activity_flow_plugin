package org.telegram.p004ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$6lQsSYr5slO_jvBJxggKac2g49o */
public final /* synthetic */ class C1717-$$Lambda$PassportActivity$6lQsSYr5slO_jvBJxggKac2g49o implements Runnable {
    private final /* synthetic */ PassportActivity f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ TLObject f$2;

    public /* synthetic */ C1717-$$Lambda$PassportActivity$6lQsSYr5slO_jvBJxggKac2g49o(PassportActivity passportActivity, TL_error tL_error, TLObject tLObject) {
        this.f$0 = passportActivity;
        this.f$1 = tL_error;
        this.f$2 = tLObject;
    }

    public final void run() {
        this.f$0.lambda$null$9$PassportActivity(this.f$1, this.f$2);
    }
}