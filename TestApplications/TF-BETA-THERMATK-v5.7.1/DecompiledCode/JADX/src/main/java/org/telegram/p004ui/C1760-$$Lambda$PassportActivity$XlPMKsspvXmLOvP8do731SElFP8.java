package org.telegram.p004ui;

import org.telegram.p004ui.PassportActivity.PassportActivityDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_sendVerifyPhoneCode;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$XlPMKsspvXmLOvP8do731SElFP8 */
public final /* synthetic */ class C1760-$$Lambda$PassportActivity$XlPMKsspvXmLOvP8do731SElFP8 implements Runnable {
    private final /* synthetic */ PassportActivity f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ String f$2;
    private final /* synthetic */ PassportActivityDelegate f$3;
    private final /* synthetic */ TLObject f$4;
    private final /* synthetic */ TL_account_sendVerifyPhoneCode f$5;

    public /* synthetic */ C1760-$$Lambda$PassportActivity$XlPMKsspvXmLOvP8do731SElFP8(PassportActivity passportActivity, TL_error tL_error, String str, PassportActivityDelegate passportActivityDelegate, TLObject tLObject, TL_account_sendVerifyPhoneCode tL_account_sendVerifyPhoneCode) {
        this.f$0 = passportActivity;
        this.f$1 = tL_error;
        this.f$2 = str;
        this.f$3 = passportActivityDelegate;
        this.f$4 = tLObject;
        this.f$5 = tL_account_sendVerifyPhoneCode;
    }

    public final void run() {
        this.f$0.lambda$null$66$PassportActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
