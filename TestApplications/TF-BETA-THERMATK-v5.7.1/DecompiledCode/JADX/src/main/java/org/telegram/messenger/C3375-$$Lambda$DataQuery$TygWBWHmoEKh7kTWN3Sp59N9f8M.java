package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$TygWBWHmoEKh7kTWN3Sp59N9f8M */
public final /* synthetic */ class C3375-$$Lambda$DataQuery$TygWBWHmoEKh7kTWN3Sp59N9f8M implements RequestDelegate {
    public static final /* synthetic */ C3375-$$Lambda$DataQuery$TygWBWHmoEKh7kTWN3Sp59N9f8M INSTANCE = new C3375-$$Lambda$DataQuery$TygWBWHmoEKh7kTWN3Sp59N9f8M();

    private /* synthetic */ C3375-$$Lambda$DataQuery$TygWBWHmoEKh7kTWN3Sp59N9f8M() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        DataQuery.lambda$markFaturedStickersAsRead$26(tLObject, tL_error);
    }
}
