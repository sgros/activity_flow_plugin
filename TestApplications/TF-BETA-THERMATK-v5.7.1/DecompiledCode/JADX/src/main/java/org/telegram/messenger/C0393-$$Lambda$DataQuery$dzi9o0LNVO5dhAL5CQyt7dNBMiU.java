package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_contacts_topPeers;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$dzi9o0LNVO5dhAL5CQyt7dNBMiU */
public final /* synthetic */ class C0393-$$Lambda$DataQuery$dzi9o0LNVO5dhAL5CQyt7dNBMiU implements Runnable {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ TL_contacts_topPeers f$1;

    public /* synthetic */ C0393-$$Lambda$DataQuery$dzi9o0LNVO5dhAL5CQyt7dNBMiU(DataQuery dataQuery, TL_contacts_topPeers tL_contacts_topPeers) {
        this.f$0 = dataQuery;
        this.f$1 = tL_contacts_topPeers;
    }

    public final void run() {
        this.f$0.lambda$null$71$DataQuery(this.f$1);
    }
}
