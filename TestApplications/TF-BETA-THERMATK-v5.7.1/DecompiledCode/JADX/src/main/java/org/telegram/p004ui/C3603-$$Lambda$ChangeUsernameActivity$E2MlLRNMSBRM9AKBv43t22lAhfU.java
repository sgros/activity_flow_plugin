package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChangeUsernameActivity$E2MlLRNMSBRM9AKBv43t22lAhfU */
public final /* synthetic */ class C3603-$$Lambda$ChangeUsernameActivity$E2MlLRNMSBRM9AKBv43t22lAhfU implements RequestDelegate {
    private final /* synthetic */ ChangeUsernameActivity f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C3603-$$Lambda$ChangeUsernameActivity$E2MlLRNMSBRM9AKBv43t22lAhfU(ChangeUsernameActivity changeUsernameActivity, String str) {
        this.f$0 = changeUsernameActivity;
        this.f$1 = str;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$null$3$ChangeUsernameActivity(this.f$1, tLObject, tL_error);
    }
}