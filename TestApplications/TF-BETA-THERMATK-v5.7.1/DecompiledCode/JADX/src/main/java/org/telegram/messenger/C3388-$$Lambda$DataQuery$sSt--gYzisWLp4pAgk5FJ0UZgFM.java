package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$sSt--gYzisWLp4pAgk5FJ0UZgFM */
public final /* synthetic */ class C3388-$$Lambda$DataQuery$sSt--gYzisWLp4pAgk5FJ0UZgFM implements RequestDelegate {
    private final /* synthetic */ DataQuery f$0;

    public /* synthetic */ C3388-$$Lambda$DataQuery$sSt--gYzisWLp4pAgk5FJ0UZgFM(DataQuery dataQuery) {
        this.f$0 = dataQuery;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadGroupStickerSet$8$DataQuery(tLObject, tL_error);
    }
}