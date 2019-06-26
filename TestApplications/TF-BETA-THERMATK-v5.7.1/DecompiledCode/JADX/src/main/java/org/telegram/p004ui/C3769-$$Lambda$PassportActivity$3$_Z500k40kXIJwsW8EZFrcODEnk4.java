package org.telegram.p004ui;

import org.telegram.p004ui.PassportActivity.C42563;
import org.telegram.p004ui.PassportActivity.ErrorRunnable;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_verifyEmail;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$3$_Z500k40kXIJwsW8EZFrcODEnk4 */
public final /* synthetic */ class C3769-$$Lambda$PassportActivity$3$_Z500k40kXIJwsW8EZFrcODEnk4 implements RequestDelegate {
    private final /* synthetic */ C42563 f$0;
    private final /* synthetic */ Runnable f$1;
    private final /* synthetic */ ErrorRunnable f$2;
    private final /* synthetic */ TL_account_verifyEmail f$3;

    public /* synthetic */ C3769-$$Lambda$PassportActivity$3$_Z500k40kXIJwsW8EZFrcODEnk4(C42563 c42563, Runnable runnable, ErrorRunnable errorRunnable, TL_account_verifyEmail tL_account_verifyEmail) {
        this.f$0 = c42563;
        this.f$1 = runnable;
        this.f$2 = errorRunnable;
        this.f$3 = tL_account_verifyEmail;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$onItemClick$6$PassportActivity$3(this.f$1, this.f$2, this.f$3, tLObject, tL_error);
    }
}
