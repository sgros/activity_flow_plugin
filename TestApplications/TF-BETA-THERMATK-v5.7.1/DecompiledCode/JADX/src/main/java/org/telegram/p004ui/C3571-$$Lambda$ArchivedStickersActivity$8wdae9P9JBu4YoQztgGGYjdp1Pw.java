package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ArchivedStickersActivity$8wdae9P9JBu4YoQztgGGYjdp1Pw */
public final /* synthetic */ class C3571-$$Lambda$ArchivedStickersActivity$8wdae9P9JBu4YoQztgGGYjdp1Pw implements RequestDelegate {
    private final /* synthetic */ ArchivedStickersActivity f$0;

    public /* synthetic */ C3571-$$Lambda$ArchivedStickersActivity$8wdae9P9JBu4YoQztgGGYjdp1Pw(ArchivedStickersActivity archivedStickersActivity) {
        this.f$0 = archivedStickersActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$getStickers$2$ArchivedStickersActivity(tLObject, tL_error);
    }
}
