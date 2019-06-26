package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$bn-59i2M3GKJW8EWB0ORPpDQN_w */
public final /* synthetic */ class C3380-$$Lambda$DataQuery$bn-59i2M3GKJW8EWB0ORPpDQN_w implements RequestDelegate {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C3380-$$Lambda$DataQuery$bn-59i2M3GKJW8EWB0ORPpDQN_w(DataQuery dataQuery, int i) {
        this.f$0 = dataQuery;
        this.f$1 = i;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadPinnedMessageInternal$87$DataQuery(this.f$1, tLObject, tL_error);
    }
}
