package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LocationActivity$w8_XrXsKbv633ZrXpPVuZnH-vEE */
public final /* synthetic */ class C3731-$$Lambda$LocationActivity$w8_XrXsKbv633ZrXpPVuZnH-vEE implements RequestDelegate {
    private final /* synthetic */ LocationActivity f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ C3731-$$Lambda$LocationActivity$w8_XrXsKbv633ZrXpPVuZnH-vEE(LocationActivity locationActivity, long j) {
        this.f$0 = locationActivity;
        this.f$1 = j;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$getRecentLocations$11$LocationActivity(this.f$1, tLObject, tL_error);
    }
}
