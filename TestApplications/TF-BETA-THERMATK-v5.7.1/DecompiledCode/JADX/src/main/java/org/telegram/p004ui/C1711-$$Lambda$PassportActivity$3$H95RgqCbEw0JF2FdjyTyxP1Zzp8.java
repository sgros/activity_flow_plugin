package org.telegram.p004ui;

import org.telegram.p004ui.PassportActivity.C42563;
import org.telegram.p004ui.PassportActivity.ErrorRunnable;
import org.telegram.tgnet.TLRPC.TL_account_verifyEmail;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$3$H95RgqCbEw0JF2FdjyTyxP1Zzp8 */
public final /* synthetic */ class C1711-$$Lambda$PassportActivity$3$H95RgqCbEw0JF2FdjyTyxP1Zzp8 implements Runnable {
    private final /* synthetic */ C42563 f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ Runnable f$2;
    private final /* synthetic */ ErrorRunnable f$3;
    private final /* synthetic */ TL_account_verifyEmail f$4;

    public /* synthetic */ C1711-$$Lambda$PassportActivity$3$H95RgqCbEw0JF2FdjyTyxP1Zzp8(C42563 c42563, TL_error tL_error, Runnable runnable, ErrorRunnable errorRunnable, TL_account_verifyEmail tL_account_verifyEmail) {
        this.f$0 = c42563;
        this.f$1 = tL_error;
        this.f$2 = runnable;
        this.f$3 = errorRunnable;
        this.f$4 = tL_account_verifyEmail;
    }

    public final void run() {
        this.f$0.lambda$null$5$PassportActivity$3(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
