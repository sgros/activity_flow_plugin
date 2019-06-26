package org.telegram.p004ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_getAttachedStickers;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$StickersAlert$R4T4Ne-ypM57cFmDbPuLpbHC0Ro */
public final /* synthetic */ class C4063-$$Lambda$StickersAlert$R4T4Ne-ypM57cFmDbPuLpbHC0Ro implements RequestDelegate {
    private final /* synthetic */ StickersAlert f$0;
    private final /* synthetic */ TL_messages_getAttachedStickers f$1;

    public /* synthetic */ C4063-$$Lambda$StickersAlert$R4T4Ne-ypM57cFmDbPuLpbHC0Ro(StickersAlert stickersAlert, TL_messages_getAttachedStickers tL_messages_getAttachedStickers) {
        this.f$0 = stickersAlert;
        this.f$1 = tL_messages_getAttachedStickers;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$new$1$StickersAlert(this.f$1, tLObject, tL_error);
    }
}
