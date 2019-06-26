package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$vXxSlr6eK9Z6xNmLs0Dj7UWvraQ */
public final /* synthetic */ class C3392-$$Lambda$DataQuery$vXxSlr6eK9Z6xNmLs0Dj7UWvraQ implements RequestDelegate {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C3392-$$Lambda$DataQuery$vXxSlr6eK9Z6xNmLs0Dj7UWvraQ(DataQuery dataQuery, int i) {
        this.f$0 = dataQuery;
        this.f$1 = i;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadArchivedStickersCount$30$DataQuery(this.f$1, tLObject, tL_error);
    }
}
