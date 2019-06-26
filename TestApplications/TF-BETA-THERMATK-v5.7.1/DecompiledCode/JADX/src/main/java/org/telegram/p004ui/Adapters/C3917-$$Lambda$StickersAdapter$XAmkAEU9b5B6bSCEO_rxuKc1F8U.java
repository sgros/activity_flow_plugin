package org.telegram.p004ui.Adapters;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$StickersAdapter$XAmkAEU9b5B6bSCEO_rxuKc1F8U */
public final /* synthetic */ class C3917-$$Lambda$StickersAdapter$XAmkAEU9b5B6bSCEO_rxuKc1F8U implements RequestDelegate {
    private final /* synthetic */ StickersAdapter f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C3917-$$Lambda$StickersAdapter$XAmkAEU9b5B6bSCEO_rxuKc1F8U(StickersAdapter stickersAdapter, String str) {
        this.f$0 = stickersAdapter;
        this.f$1 = str;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$searchServerStickers$3$StickersAdapter(this.f$1, tLObject, tL_error);
    }
}
